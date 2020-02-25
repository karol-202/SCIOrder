package pl.karol202.sciorder.server.service.permission

import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.entity.AdminEntity
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.entity.UserEntity

class PermissionServiceImpl : PermissionService
{
	override fun isValidPrincipal(principal: AbstractPrincipal) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> doesAdminExist(principal.adminId)
		is AbstractPrincipal.UserPrincipal -> doesUserExist(principal.userId) && doesStoreExist(principal.storeId)
	}
	
	override fun canInsertProduct(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canUpdateProduct(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canDeleteProduct(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canGetProducts(principal: AbstractPrincipal, storeId: Long) = handleUserActionInStore(principal, storeId)
	
	override fun canInsertProductParameter(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canUpdateProductParameter(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canDeleteProductParameter(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canGetProductParameters(principal: AbstractPrincipal, storeId: Long) = handleUserActionInStore(principal, storeId)
	
	override fun canInsertOrder(principal: AbstractPrincipal, storeId: Long) = handleUserActionInStore(principal, storeId)
	
	override fun canUpdateOrderStatus(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canDeleteOrders(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canGetAllOrders(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canGetOwnOrders(principal: AbstractPrincipal, storeId: Long) = handleUserActionInStore(principal, storeId)
	
	override fun canInsertStore(principal: AbstractPrincipal) = handleAdminAction(principal)
	
	override fun canDeleteStore(principal: AbstractPrincipal, storeId: Long) = handleAdminActionInStore(principal, storeId)
	
	override fun canDeleteAdmin(principal: AbstractPrincipal, adminId: Long) = handleAdminActionOnAdmin(principal, adminId)
	
	private fun handleAdminAction(principal: AbstractPrincipal) =
			principal is AbstractPrincipal.AdminPrincipal && isValidPrincipal(principal)
	
	private fun handleAdminActionOnAdmin(principal: AbstractPrincipal, adminId: Long) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> principal.adminId == adminId
		is AbstractPrincipal.UserPrincipal -> false
	} && isValidPrincipal(principal)
	
	private fun handleAdminActionInStore(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> hasAdminRightsToStore(principal.adminId, storeId)
		is AbstractPrincipal.UserPrincipal -> false
	} && isValidPrincipal(principal)
	
	private fun handleUserActionInStore(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.AdminPrincipal -> true
		is AbstractPrincipal.UserPrincipal -> storeId == principal.storeId
	} && isValidPrincipal(principal)
	
	private fun hasAdminRightsToStore(adminId: Long, storeId: Long) =
			AdminEntity.findById(adminId)?.stores?.any { it.id.value == storeId } ?: false
	
	private fun doesAdminExist(adminId: Long) = AdminEntity.findById(adminId) != null
	
	private fun doesUserExist(userId: Long) = UserEntity.findById(userId) != null
	
	private fun doesStoreExist(storeId: Long) = StoreEntity.findById(storeId) != null
}
