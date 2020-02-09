package pl.karol202.sciorder.client.common.api.product

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.Product

interface ProductApi
{
	suspend fun addProduct(ownerId: String, hash: String, product: Product): ApiResponse<Product>

	suspend fun updateProduct(ownerId: String, productId: String, hash: String, product: Product): ApiResponse<Unit>

	suspend fun removeProduct(ownerId: String, productId: String, hash: String): ApiResponse<Unit>

	suspend fun getAllProducts(ownerId: String): ApiResponse<List<Product>>
}
