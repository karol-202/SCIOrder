package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.model.Product

class DatabaseProductDao : ProductDao
{
    private val productsCollection = Database.getCollection<Product>()

	override suspend fun addProduct(product: Product)
	{
		productsCollection.insertOne(product)
	}

	override suspend fun getAllProducts() = productsCollection.find().toList()

	override suspend fun getProductById(id: String) = productsCollection.findOneById(id)
}
