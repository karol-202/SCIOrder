package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.Owner
import retrofit2.http.*

interface RetrofitOwnerApi
{
	@POST("owner")
	suspend fun addOwner(@Body owner: Owner): ApiResponse<Owner>

	@PUT("owner/{ownerId}/hash")
	suspend fun updateOwnerHash(@Path("ownerId") ownerId: String,
	                            @Query("hash") hash: String,
	                            @Query("newHash") newHash: String): ApiResponse<Unit>

	@GET("owner")
	suspend fun getOwnerByName(@Query("name") name: String,
	                           @Query("hash") hash: String?): ApiResponse<Owner>
}

// RetrofitOwnerApi cannot extend OwnerApi thanks to Jake Wharton
fun RetrofitOwnerApi.asOwnerApi() = object : OwnerApi {
	override suspend fun addOwner(owner: Owner) =
			this@asOwnerApi.addOwner(owner)

	override suspend fun updateOwnerHash(ownerId: String, hash: String, newHash: String) =
			this@asOwnerApi.updateOwnerHash(ownerId, hash, newHash)

	override suspend fun getOwnerByName(name: String, hash: String?) =
			this@asOwnerApi.getOwnerByName(name, hash)
}
