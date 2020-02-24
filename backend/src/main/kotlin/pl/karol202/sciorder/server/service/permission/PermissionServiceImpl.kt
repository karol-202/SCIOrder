package pl.karol202.sciorder.server.service.permission

import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.entity.AdminEntity

class PermissionServiceImpl : PermissionService
{
	override fun canInsertProduct(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canUpdateProduct(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canDeleteProduct(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canGetProducts(principal: AbstractPrincipal, storeId: Long) = handleUserAction(principal, storeId)
	
	override fun canInsertProductParameter(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canUpdateProductParameter(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canDeleteProductParameter(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canGetProductParameters(principal: AbstractPrincipal, storeId: Long) = handleUserAction(principal, storeId)
	
	override fun canInsertOrder(principal: AbstractPrincipal, storeId: Long) = handleUserAction(principal, storeId)
	
	override fun canUpdateOrderStatus(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canDeleteOrders(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canGetAllOrders(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	override fun canInsertStore(principal: AbstractPrincipal) = handleAdminAction(principal)
	
	override fun canDeleteStore(principal: AbstractPrincipal, storeId: Long) = handleAdminAction(principal, storeId)
	
	private fun handleAdminAction(principal: AbstractPrincipal) = principal is AbstractPrincipal.AdminPrincipal
	
	private fun handleAdminAction(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.StorePrincipal -> false
		is AbstractPrincipal.AdminPrincipal -> hasAdminRightsToStore(principal.adminId, storeId)
	}
	
	private fun handleUserAction(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.StorePrincipal -> storeId == principal.storeId
		is AbstractPrincipal.AdminPrincipal -> doesAdminExist(principal.adminId)
	}
	
	private fun hasAdminRightsToStore(adminId: Long, storeId: Long) =
			AdminEntity.findById(adminId)?.stores?.any { it.id.value == storeId } ?: false
	
	private fun doesAdminExist(adminId: Long) = AdminEntity.findById(adminId) != null
}
