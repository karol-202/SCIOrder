package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.model.Order

interface OrderDao
{
	suspend fun addOrder(order: Order)

	suspend fun getAllOrders(): List<Order>

	suspend fun getOrdersById(ids: List<String>): List<Order>
}
