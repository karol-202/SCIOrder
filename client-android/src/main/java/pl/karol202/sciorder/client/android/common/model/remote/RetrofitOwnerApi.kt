package pl.karol202.sciorder.client.android.common.model.remote

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.common.model.Owner
import retrofit2.http.*

interface RetrofitOwnerApi : OwnerApi
{
	@POST("owner")
	override fun addOwner(@Body owner: Owner): ApiResponse<Owner>

	@PUT("owner/{ownerId}/hash")
	override fun updateOwnerHash(@Path("ownerId") ownerId: String,
	                             @Query("hash") hash: String,
	                             @Query("newHash") newHash: String): ApiResponse<Unit>

	@GET("owner")
	override fun getOwnerByName(@Query("name") name: String,
	                            @Query("hash") hash: String?): ApiResponse<Owner>
}
