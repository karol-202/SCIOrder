package pl.karol202.sciorder.client.common.model.remote

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.http.*

interface ProductApi
{
	@POST("owner/{ownerId}/products")
	fun addProduct(@Path("ownerId") ownerId: String,
	               @Body product: Product):
			LiveData<ApiResponse<Product>>

	@PUT("owner/{ownerId}/products/{productId}")
	fun updateProduct(@Path("ownerId") ownerId: String,
	                  @Path("productId") productId: String,
	                  @Body product: Product):
			LiveData<ApiResponse<Unit>>

	@DELETE("owner/{ownerId}/products/{productId}")
	fun removeProduct(@Path("ownerId") ownerId: String,
	                  @Path("productId") productId: String):
			LiveData<ApiResponse<Unit>>

	@GET("owner/{ownerId}/products")
	fun getAllProducts(@Path("ownerId") ownerId: String):
			LiveData<ApiResponse<List<Product>>>
}
