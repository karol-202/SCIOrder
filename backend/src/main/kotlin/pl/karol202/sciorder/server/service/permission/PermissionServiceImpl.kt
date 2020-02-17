package pl.karol202.sciorder.server.service.permission

import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.entity.AdminEntity

class PermissionServiceImpl : PermissionService
{
	override fun canInsertProduct(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.StorePrincipal -> false
		is AbstractPrincipal.AdminPrincipal -> hasAdminRightsToStore(principal.adminId, storeId)
	}
	
	override fun canPutProduct(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.StorePrincipal -> false
		is AbstractPrincipal.AdminPrincipal -> hasAdminRightsToStore(principal.adminId, storeId)
	}
	
	override fun canDeleteProducts(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.StorePrincipal -> false
		is AbstractPrincipal.AdminPrincipal -> hasAdminRightsToStore(principal.adminId, storeId)
	}
	
	override fun canGetProducts(principal: AbstractPrincipal, storeId: Long) = when(principal)
	{
		is AbstractPrincipal.StorePrincipal -> storeId == principal.storeId
		is AbstractPrincipal.AdminPrincipal -> doesAdminExist(principal.adminId)
	}
	
	private fun hasAdminRightsToStore(adminId: Long, storeId: Long) =
			AdminEntity.findById(adminId)?.stores?.any { it.id.value == storeId } ?: false
	
	private fun doesAdminExist(adminId: Long) = AdminEntity.findById(adminId) != null
}
