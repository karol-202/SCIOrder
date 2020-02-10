package pl.karol202.sciorder.server.database

import org.litote.kmongo.and
import org.litote.kmongo.setValue
import pl.karol202.sciorder.common.model.Order

class DatabaseOrderDao(database: KMongoDatabase) : OrderDao
{
    private val ordersCollection = database.getCollection<Order>()

	override suspend fun insertOrder(order: Order)
	{
		ordersCollection.insertOne(order)
	}

	override suspend fun updateOrderStatus(ownerId: String, id: String, status: Order.Status) =
		ordersCollection.updateOne(and(Order::ownerId eq ownerId, Order::_id eq id),
		                           setValue(Order::status, status))
			.let { it.wasAcknowledged() && it.matchedCount > 0 }

	override suspend fun deleteOrdersByOwner(ownerId: String)
	{
		ordersCollection.deleteMany(Order::ownerId eq ownerId)
	}

	override suspend fun getOrdersByOwner(ownerId: String) =
			ordersCollection.find(Order::ownerId eq ownerId).toList()

	override suspend fun getOrdersById(ownerId: String, ids: List<String>) =
			ordersCollection.find(Order::ownerId eq ownerId, Order::_id `in` ids).toList()
}
