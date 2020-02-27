package pl.karol202.sciorder.server.controller.order

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest
import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.controller.*
import pl.karol202.sciorder.server.service.order.OrderService
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.product.ProductService
import pl.karol202.sciorder.server.validation.isValid

class OrderControllerImpl(private val permissionService: PermissionService,
                          private val orderService: OrderService,
                          private val productService: ProductService) : OrderController
{
	override suspend fun postOrder(requestHandler: RequestHandler) = requestHandler {
		val storeId = requireLongParameter("storeId")
		val order = requireBody<OrderRequest> { isValid { productService.getProduct(storeId, it) } }
		requirePrincipal { permissionService.canInsertOrder(it, storeId) }
		
		val newOrder = orderService.insertOrder(storeId, order)
		created(newOrder)
	}
	
	override suspend fun putOrderStatus(requestHandler: RequestHandler) = requestHandler {
		val storeId = requireLongParameter("storeId")
		val orderId = requireLongParameter("orderId")
		val status = requireBody<Order.Status>()
		requirePrincipal { permissionService.canUpdateOrderStatus(it, storeId) }
		
		orderService.updateOrderStatus(storeId, orderId, status)
		ok()
	}
	
	override suspend fun deleteOrders(requestHandler: RequestHandler) = requestHandler {
		val storeId = requireLongParameter("storeId")
		requirePrincipal { permissionService.canDeleteOrders(it, storeId) }
		
		orderService.deleteOrders(storeId)
		ok()
	}
	
	override suspend fun getOrders(requestHandler: RequestHandler) = requestHandler {
		val storeId = requireLongParameter("storeId")
		val principal = requirePrincipal { permissionService.canUsePrincipal(it) }
		
		val orders = when
		{
			principal is AbstractPrincipal.AdminPrincipal && permissionService.canGetAllOrders(principal, storeId) ->
				orderService.getAllOrders(storeId)
			principal is AbstractPrincipal.UserPrincipal && permissionService.canGetOwnOrders(principal, storeId) ->
				orderService.getOrdersByUser(storeId, principal.userId)
			else -> internalError() // Should not happen with proper permission service
		}
		ok(orders)
	}
}
