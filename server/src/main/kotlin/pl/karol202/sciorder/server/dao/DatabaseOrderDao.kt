package pl.karol202.sciorder.server.dao

import org.litote.kmongo.`in`
import pl.karol202.sciorder.common.model.Order

class DatabaseOrderDao : OrderDao
{
    private val ordersCollection = Database.getCollection<Order>()

	override suspend fun addOrder(order: Order)
	{
		ordersCollection.insertOne(order)
	}

	override suspend fun getAllOrders() = ordersCollection.find().toList()

	override suspend fun getOrdersById(ids: List<String>) = ordersCollection.find(Order::_id `in` ids).toList()
}
