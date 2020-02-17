package pl.karol202.sciorder.server.service.permission

import pl.karol202.sciorder.server.auth.AbstractPrincipal

interface PermissionService
{
	fun canInsertProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canPutProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canDeleteProducts(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canGetProducts(principal: AbstractPrincipal, storeId: Long): Boolean
}
