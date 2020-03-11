package pl.karol202.sciorder.client.common.api.product

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductRequest

interface ProductApi
{
	suspend fun addProduct(token: String, storeId: Long, product: ProductRequest): ApiResponse<Product>

	suspend fun updateProduct(token: String, storeId: Long, productId: Long, product: ProductRequest): ApiResponse<Unit>

	suspend fun removeProduct(token: String, storeId: Long, productId: Long): ApiResponse<Unit>

	suspend fun getProducts(token: String, storeId: Long): ApiResponse<List<Product>>
}
