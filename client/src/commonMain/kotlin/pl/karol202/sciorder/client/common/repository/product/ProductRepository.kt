package pl.karol202.sciorder.client.common.repository.product

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.Owner
import pl.karol202.sciorder.common.Product

interface ProductRepository
{
	fun getProductsResource(ownerId: String): Resource<List<Product>>

	suspend fun addProduct(owner: Owner, product: Product): ApiResponse<Product>

	suspend fun updateProduct(owner: Owner, product: Product): ApiResponse<Unit>

	suspend fun removeProduct(owner: Owner, product: Product): ApiResponse<Unit>
}
