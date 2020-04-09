package pl.karol202.sciorder.client.common.repository.product

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest

interface ProductRepository
{
	fun getProductsResource(token: String, storeId: Long): Resource<List<Product>>

	suspend fun addProduct(token: String, storeId: Long, product: ProductCreateRequest): ApiResponse<Product>

	suspend fun updateProduct(token: String, storeId: Long, productId: Long, product: ProductUpdateRequest): ApiResponse<Product>

	suspend fun removeProduct(token: String, storeId: Long, productId: Long): ApiResponse<Unit>
	
	suspend fun cleanLocalProducts()
}
