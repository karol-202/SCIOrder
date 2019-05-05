package pl.karol202.sciorder.server.dao

import org.litote.kmongo.`in`
import org.litote.kmongo.eq
import org.litote.kmongo.set
import pl.karol202.sciorder.common.model.Order

class DatabaseOrderDao : OrderDao
{
    private val ordersCollection = Database.getCollection<Order>()

	override suspend fun addOrder(order: Order)
	{
		ordersCollection.insertOne(order)
	}

	override suspend fun updateOrderStatus(id: String, status: Order.Status) =
		ordersCollection.updateOne(Order::_id eq id, set(Order::status, status))
			.let { it.wasAcknowledged() && it.matchedCount > 0 }

	override suspend fun getAllOrders() = ordersCollection.find().toList()

	override suspend fun getOrdersById(ids: List<String>) = ordersCollection.find(Order::_id `in` ids).toList()
}
