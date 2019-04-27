package pl.karol202.sciorder.server.dao

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.newId
import org.litote.kmongo.reactivestreams.KMongo
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.Product

class DatabaseDao : Dao
{
    private val client = KMongo.createClient().coroutine
    private val database = client.getDatabase("sciorder")

    private val ordersCollection = database.getCollection<Order>()
    private val productsCollection = database.getCollection<Product>()

	override suspend fun addOrder(order: Order)
	{
		ordersCollection.insertOne(order)
	}

	override suspend fun getAllOrders() = ordersCollection.find().toList()

	override suspend fun addProduct(product: Product)
	{
		productsCollection.insertOne(product)
	}

	override suspend fun getAllProducts() = productsCollection.find().toList()

	override suspend fun getProductOfId(id: String) = productsCollection.findOneById(id)
}