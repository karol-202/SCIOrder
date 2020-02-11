package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.common.model.Order

interface OrderDao
{
	suspend fun insertOrder(order: Order)

	suspend fun updateOrderStatus(storeId: Int, orderId: Int, status: Order.Status): Boolean

	suspend fun deleteOrdersByStore(storeId: Int)

	suspend fun getOrdersByStore(storeId: Int): List<Order>

	suspend fun getOrdersByStoreAndIds(storeId: Int, ids: List<Int>): List<Order>
}
