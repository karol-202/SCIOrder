package pl.karol202.sciorder.client.common.model.remote

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Owner
import retrofit2.http.*

interface OwnerApi
{
	companion object
	{
		const val RESPONSE_NOT_FOUND = 404
		const val RESPONSE_CONFLICT = 409
	}

	@POST("owner")
	fun addOwner(@Body owner: Owner): LiveData<ApiResponse<Owner>>

	@PUT("owner/{ownerId}/hash")
	fun updateOwnerHash(@Path("ownerId") ownerId: String,
	                    @Query("hash") hash: String):
			LiveData<ApiResponse<Unit>>

	@GET("owner")
	fun getOwnerByName(@Query("name") name: String,
	                   @Query("hash") hash: String? = null): LiveData<ApiResponse<Owner>>
}
