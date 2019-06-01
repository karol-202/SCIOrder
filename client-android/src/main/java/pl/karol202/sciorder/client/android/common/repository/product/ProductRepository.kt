package pl.karol202.sciorder.client.android.common.repository.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.android.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.android.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Owner
import pl.karol202.sciorder.common.model.Product

interface ProductRepository
{
	fun getAllProducts(ownerId: String): Resource<List<Product>>

	fun addProduct(owner: Owner, product: Product): LiveData<ApiResponse<Product>>

	fun updateProduct(owner: Owner, product: Product): LiveData<ApiResponse<Unit>>

	fun removeProduct(owner: Owner, product: Product): LiveData<ApiResponse<Unit>>
}
