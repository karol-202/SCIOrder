package pl.karol202.sciorder.server.database

import pl.karol202.sciorder.common.model.Order

interface OrderDao
{
	suspend fun addOrder(order: Order)

	suspend fun updateOrderStatus(id: String, status: Order.Status): Boolean

	suspend fun removeAllOrders()

	suspend fun getAllOrders(): List<Order>

	suspend fun getOrdersById(ids: List<String>): List<Order>
}
