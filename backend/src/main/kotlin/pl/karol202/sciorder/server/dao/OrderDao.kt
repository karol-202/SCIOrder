package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.common.model.Order

interface OrderDao
{
	suspend fun insertOrder(order: Order): Order

	suspend fun updateOrderStatus(orderId: Int, status: Order.Status)

	suspend fun deleteOrdersByStore(storeId: Int)

	suspend fun getOrdersByStore(storeId: Int): List<Order>

	suspend fun getOrdersByIds(orderIds: List<Int>): List<Order>
}
