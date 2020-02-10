package pl.karol202.sciorder.client.common.repository.product

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Product

interface ProductRepository
{
	fun getProductsResource(ownerId: String): Resource<List<Product>>

	suspend fun addProduct(owner: Owner, product: Product): ApiResponse<Product>

	suspend fun updateProduct(owner: Owner, product: Product): ApiResponse<Unit>

	suspend fun removeProduct(owner: Owner, product: Product): ApiResponse<Unit>
}
