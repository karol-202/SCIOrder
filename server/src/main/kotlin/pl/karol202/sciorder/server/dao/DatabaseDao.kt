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

	override suspend fun createOrder(entries: List<Order.Entry>)
	{
		val order = Order(newStringId<Order>(), entries, Order.Status.WAITING)
		ordersCollection.insertOne(order)
	}

	override suspend fun getAllOrders() = ordersCollection.find().toList()

	override suspend fun createProduct(name: String, available: Boolean, parameters: List<Product.Parameter>)
	{
		val product = Product(newStringId<Product>(), name, available, parameters)
		productsCollection.insertOne(product)
	}

	override suspend fun getAllProducts() = productsCollection.find().toList()

	override suspend fun getProductOfId(id: String) = productsCollection.findOneById(id)

	private fun <T> newStringId() = newId<T>().toString()
}