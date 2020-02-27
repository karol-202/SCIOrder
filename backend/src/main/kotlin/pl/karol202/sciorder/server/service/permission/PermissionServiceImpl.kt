package pl.karol202.sciorder.server.service.permission

import org.jetbrains.exposed.sql.Transaction
import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.entity.AdminEntity
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.entity.UserEntity
import pl.karol202.sciorder.server.service.transaction.TransactionService

class PermissionServiceImpl(private val transactionService: TransactionService) : PermissionService
{
	override suspend fun canUsePrincipal(principal: AbstractPrincipal) =
			transaction { isPrincipalValid(principal) }
	
	override suspend fun canInsertProduct(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canUpdateProduct(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canDeleteProduct(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canGetProducts(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleUserActionInStore(principal, storeId) }
	
	override suspend fun canInsertProductParameter(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canUpdateProductParameter(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canDeleteProductParameter(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canGetProductParameters(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleUserActionInStore(principal, storeId) }
	
	override suspend fun canInsertOrder(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleUserActionInStore(principal, storeId) }
	
	override suspend fun canUpdateOrderStatus(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canDeleteOrders(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canGetAllOrders(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canGetOwnOrders(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleUserActionInStore(principal, storeId) }
	
	override suspend fun canInsertStore(principal: AbstractPrincipal) =
			transaction { handleAdminAction(principal) }
	
	override suspend fun canDeleteStore(principal: AbstractPrincipal, storeId: Long) =
			transaction { handleAdminActionInStore(principal, storeId) }
	
	override suspend fun canDeleteAdmin(principal: AbstractPrincipal, adminId: Long) =
			transaction { handleAdminActionOnAdmin(principal, adminId) }
	
	private fun isPrincipalValid(principal: AbstractPrincipal) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> doesAdminExist(principal.adminId)
		is AbstractPrincipal.UserPrincipal -> doesUserExist(principal.userId) && doesStoreExist(principal.storeId)
	}
	
	private fun handleAdminAction(principal: AbstractPrincipal) =
			principal is AbstractPrincipal.AdminPrincipal && isPrincipalValid(principal)
	
	private fun handleAdminActionOnAdmin(principal: AbstractPrincipal, adminId: Long) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> principal.adminId == adminId
		is AbstractPrincipal.UserPrincipal -> false
	} && isPrincipalValid(principal)
	
	private fun handleAdminActionInStore(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> hasAdminRightsToStore(principal.adminId, storeId)
		is AbstractPrincipal.UserPrincipal -> false
	} && isPrincipalValid(principal)
	
	private fun handleUserActionInStore(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> true
		is AbstractPrincipal.UserPrincipal -> storeId == principal.storeId
	} && isPrincipalValid(principal)
	
	private fun hasAdminRightsToStore(adminId: Long, storeId: Long) =
			AdminEntity.findById(adminId)?.stores?.any { it.id.value == storeId } ?: false
	
	private fun doesAdminExist(adminId: Long) = AdminEntity.findById(adminId) != null
	
	private fun doesUserExist(userId: Long) = UserEntity.findById(userId) != null
	
	private fun doesStoreExist(storeId: Long) = StoreEntity.findById(storeId) != null
	
	private suspend fun transaction(block: suspend Transaction.() -> Boolean) = transactionService.runTransaction(block)
}
