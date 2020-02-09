package pl.karol202.sciorder.server.database

import pl.karol202.sciorder.common.model.Order

interface OrderDao
{
	suspend fun insertOrder(order: Order)

	suspend fun updateOrderStatus(ownerId: String, id: String, status: Order.Status): Boolean

	suspend fun deleteOrdersByOwner(ownerId: String)

	suspend fun getOrdersByOwner(ownerId: String): List<Order>

	suspend fun getOrdersById(ownerId: String, ids: List<String>): List<Order>
}
