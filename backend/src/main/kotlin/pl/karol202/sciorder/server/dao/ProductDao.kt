package pl.karol202.sciorder.server.dao

import pl.karol202.sciorder.common.model.Product

interface ProductDao
{
	suspend fun insertProduct(product: Product): Product

	suspend fun updateProduct(product: Product)

	suspend fun deleteProduct(productId: Int)

	suspend fun getProductsByStore(storeId: Int): List<Product>

	suspend fun getProductById(productId: Int): Product?
}
