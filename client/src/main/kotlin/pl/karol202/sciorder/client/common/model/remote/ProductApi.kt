package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.model.Product

interface ProductApi
{
	fun addProduct(ownerId: String, hash: String, product: Product): ApiResponse<Product>

	fun updateProduct(ownerId: String, productId: String, hash: String, product: Product): ApiResponse<Unit>

	fun removeProduct(ownerId: String, productId: String, hash: String): ApiResponse<Unit>

	fun getAllProducts(ownerId: String): ApiResponse<List<Product>>
}
