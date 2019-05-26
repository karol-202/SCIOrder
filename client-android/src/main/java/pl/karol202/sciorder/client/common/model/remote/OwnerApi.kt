package pl.karol202.sciorder.client.common.model.remote

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Owner
import retrofit2.http.*

interface OwnerApi
{
	@POST("owner")
	fun addOwner(owner: Owner): LiveData<ApiResponse<Owner>>

	@PUT("owner/{ownerId}/hash")
	fun updateOwnerHash(@Path("ownerId") ownerId: String,
	                    @Query("hash") hash: String):
			LiveData<ApiResponse<Unit>>

	@GET("owner")
	fun getOwnerByName(@Query("name") name: String): LiveData<ApiResponse<Owner>>
}
