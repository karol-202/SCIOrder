package pl.karol202.sciorder.client.common.model.remote.product

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.common.Product

interface ProductApi
{
	suspend fun addProduct(ownerId: String, hash: String, product: Product): ApiResponse<Product>

	suspend fun updateProduct(ownerId: String, productId: String, hash: String, product: Product): ApiResponse<Unit>

	suspend fun removeProduct(ownerId: String, productId: String, hash: String): ApiResponse<Unit>

	suspend fun getAllProducts(ownerId: String): ApiResponse<List<Product>>
}
