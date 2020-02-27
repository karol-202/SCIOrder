package pl.karol202.sciorder.server.service.permission

import pl.karol202.sciorder.server.auth.AbstractPrincipal

interface PermissionService
{
	suspend fun canUsePrincipal(principal: AbstractPrincipal): Boolean
	
	suspend fun canInsertProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canUpdateProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canDeleteProduct(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canGetProducts(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canInsertProductParameter(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canUpdateProductParameter(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canDeleteProductParameter(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canGetProductParameters(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canInsertOrder(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canUpdateOrderStatus(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canDeleteOrders(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canGetAllOrders(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canGetOwnOrders(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canInsertStore(principal: AbstractPrincipal): Boolean
	
	suspend fun canDeleteStore(principal: AbstractPrincipal, storeId: Long): Boolean
	
	suspend fun canDeleteAdmin(principal: AbstractPrincipal, adminId: Long): Boolean
}
