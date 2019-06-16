package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.Owner
import retrofit2.http.*

interface RetrofitOwnerApi : OwnerApi
{
	@POST("owner")
	override suspend fun addOwner(@Body owner: Owner): ApiResponse<Owner>

	@PUT("owner/{ownerId}/hash")
	override suspend fun updateOwnerHash(@Path("ownerId") ownerId: String,
	                                     @Query("hash") hash: String,
	                                     @Query("newHash") newHash: String): ApiResponse<Unit>

	@GET("owner")
	override suspend fun getOwnerByName(@Query("name") name: String,
	                                    @Query("hash") hash: String?): ApiResponse<Owner>
}
