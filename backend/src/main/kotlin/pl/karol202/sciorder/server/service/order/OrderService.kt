package pl.karol202.sciorder.server.service.order

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest

interface OrderService
{
	suspend fun insertOrder(storeId: Long, order: OrderRequest): Order
	
	suspend fun updateOrderStatus(storeId: Long, orderId: Long, status: Order.Status)
	
	suspend fun deleteOrders(storeId: Long)
	
	suspend fun getAllOrders(storeId: Long): List<Order>
}
