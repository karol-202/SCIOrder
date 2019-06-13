package pl.karol202.sciorder.client.android.common.model.remote

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.ProductApi
import pl.karol202.sciorder.common.model.Product
import retrofit2.http.*

interface RetrofitProductApi : ProductApi
{
	@POST("owner/{ownerId}/products")
	override fun addProduct(@Path("ownerId") ownerId: String,
	                        @Query("hash") hash: String,
	                        @Body product: Product): ApiResponse<Product>

	@PUT("owner/{ownerId}/products/{productId}")
	override fun updateProduct(@Path("ownerId") ownerId: String,
	                           @Path("productId") productId: String,
	                           @Query("hash") hash: String,
	                           @Body product: Product): ApiResponse<Unit>

	@DELETE("owner/{ownerId}/products/{productId}")
	override fun removeProduct(@Path("ownerId") ownerId: String,
	                           @Path("productId") productId: String,
	                           @Query("hash") hash: String): ApiResponse<Unit>

	@GET("owner/{ownerId}/products")
	override fun getAllProducts(@Path("ownerId") ownerId: String): ApiResponse<List<Product>>
}
