package pl.karol202.sciorder.server.dao

import org.litote.kmongo.coroutine.updateOne
import pl.karol202.sciorder.common.model.Product

class DatabaseProductDao : ProductDao
{
    private val productsCollection = Database.getCollection<Product>()

	override suspend fun addProduct(product: Product)
	{
		productsCollection.insertOne(product)
	}

	override suspend fun updateProduct(product: Product) =
			productsCollection.updateOne(product).let { it.wasAcknowledged() && it.matchedCount > 0 }

	override suspend fun removeProduct(id: String) =
			productsCollection.deleteOneById(id).let { it.wasAcknowledged() && it.deletedCount > 0 }

	override suspend fun getAllProducts() = productsCollection.find().toList()

	override suspend fun getProductById(id: String) = productsCollection.findOneById(id)
}
