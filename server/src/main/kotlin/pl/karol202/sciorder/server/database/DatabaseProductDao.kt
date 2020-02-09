package pl.karol202.sciorder.server.database

import org.litote.kmongo.and
import org.litote.kmongo.eq
import pl.karol202.sciorder.common.model.Product

class DatabaseProductDao(database: KMongoDatabase) : ProductDao
{
    private val productsCollection = database.getCollection<Product>()

	override suspend fun insertProduct(product: Product)
	{
		productsCollection.insertOne(product)
	}

	override suspend fun updateProduct(ownerId: String, product: Product) =
			productsCollection.updateOne(and(Product::ownerId eq ownerId, Product::_id eq product.id), product)
					.let { it.wasAcknowledged() && it.matchedCount > 0 }

	override suspend fun deleteProduct(ownerId: String, id: String) =
			productsCollection.deleteOne(Product::ownerId eq ownerId, Product::_id eq id)
					.let { it.wasAcknowledged() && it.deletedCount > 0 }

	override suspend fun getProductsByOwner(ownerId: String) =
			productsCollection.find(Product::ownerId eq ownerId).toList()

	override suspend fun getProductById(ownerId: String, id: String) =
			productsCollection.findOne(Product::ownerId eq ownerId, Product::_id eq id)
}
