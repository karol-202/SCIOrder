package pl.karol202.sciorder.server.database

import pl.karol202.sciorder.common.model.Product

interface ProductDao
{
	suspend fun insertProduct(product: Product)

	suspend fun updateProduct(ownerId: String, product: Product): Boolean

	suspend fun deleteProduct(ownerId: String, id: String): Boolean

	suspend fun getProductsByOwner(ownerId: String): List<Product>

	suspend fun getProductById(ownerId: String, id: String): Product?
}
