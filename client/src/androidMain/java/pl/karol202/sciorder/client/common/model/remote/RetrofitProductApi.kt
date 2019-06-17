package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.Product
import retrofit2.http.*

interface RetrofitProductApi
{
	@POST("owner/{ownerId}/products")
	suspend fun addProduct(@Path("ownerId") ownerId: String,
	                       @Query("hash") hash: String,
	                       @Body product: Product): ApiResponse<Product>

	@PUT("owner/{ownerId}/products/{productId}")
	suspend fun updateProduct(@Path("ownerId") ownerId: String,
	                          @Path("productId") productId: String,
	                          @Query("hash") hash: String,
	                          @Body product: Product): ApiResponse<Unit>

	@DELETE("owner/{ownerId}/products/{productId}")
	suspend fun removeProduct(@Path("ownerId") ownerId: String,
	                          @Path("productId") productId: String,
	                          @Query("hash") hash: String): ApiResponse<Unit>

	@GET("owner/{ownerId}/products")
	suspend fun getAllProducts(@Path("ownerId") ownerId: String): ApiResponse<List<Product>>
}

// RetrofitProductApi cannot extend ProductApi thanks to Jake Wharton
fun RetrofitProductApi.asProductApi() = object : ProductApi {
	override suspend fun addProduct(ownerId: String, hash: String, product: Product) =
			this@asProductApi.addProduct(ownerId, hash, product)

	override suspend fun updateProduct(ownerId: String,
	                                   productId: String,
	                                   hash: String,
	                                   product: Product) =
			this@asProductApi.updateProduct(ownerId, productId, hash, product)

	override suspend fun removeProduct(ownerId: String, productId: String, hash: String) =
			this@asProductApi.removeProduct(ownerId, productId, hash)

	override suspend fun getAllProducts(ownerId: String) =
			this@asProductApi.getAllProducts(ownerId)
}
