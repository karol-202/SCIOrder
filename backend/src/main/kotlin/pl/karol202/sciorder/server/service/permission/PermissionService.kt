package pl.karol202.sciorder.server.service.permission

import pl.karol202.sciorder.server.auth.AbstractPrincipal

interface PermissionService
{
	fun canInsertProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canUpdateProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canDeleteProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canGetProducts(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canInsertProductParameter(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canUpdateProductParameter(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canDeleteProductParameter(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canGetProductParameters(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canInsertOrder(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canUpdateOrderStatus(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canDeleteOrders(principal: AbstractPrincipal, storeId: Long): Boolean
	
	fun canGetAllOrders(principal: AbstractPrincipal, storeId: Long): Boolean
}
