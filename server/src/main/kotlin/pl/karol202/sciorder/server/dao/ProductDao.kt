package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.common.model.Product

interface ProductDao
{
	suspend fun addProduct(product: Product)

	suspend fun updateProduct(product: Product): Boolean

	suspend fun removeProduct(id: String): Boolean

	suspend fun getAllProducts(): List<Product>

	suspend fun getProductById(id: String): Product?
}
