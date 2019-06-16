package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.Product
import retrofit2.http.*

interface RetrofitProductApi : ProductApi
{
	@POST("owner/{ownerId}/products")
	override suspend fun addProduct(@Path("ownerId") ownerId: String,
	                                @Query("hash") hash: String,
	                                @Body product: Product): ApiResponse<Product>

	@PUT("owner/{ownerId}/products/{productId}")
	override suspend fun updateProduct(@Path("ownerId") ownerId: String,
	                                   @Path("productId") productId: String,
	                                   @Query("hash") hash: String,
	                                   @Body product: Product): ApiResponse<Unit>

	@DELETE("owner/{ownerId}/products/{productId}")
	override suspend fun removeProduct(@Path("ownerId") ownerId: String,
	                                   @Path("productId") productId: String,
	                                   @Query("hash") hash: String): ApiResponse<Unit>

	@GET("owner/{ownerId}/products")
	override suspend fun getAllProducts(@Path("ownerId") ownerId: String): ApiResponse<List<Product>>
}
