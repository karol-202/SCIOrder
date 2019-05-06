package pl.karol202.sciorder.client.common.model.remote.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.http.*

interface ProductApi
{
	@PUT("products")
	fun addProduct(@Body product: Product): LiveData<ApiResponse<Product>>

	@POST("products")
	fun updateProduct(@Body product: Product): LiveData<ApiResponse<Unit>>

	@DELETE("products/{id}")
	fun removeProduct(@Path("id") productId: String): LiveData<ApiResponse<Unit>>

	@GET("products")
	fun getAllProducts(): LiveData<ApiResponse<List<Product>>>
}
