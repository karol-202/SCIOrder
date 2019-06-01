package pl.karol202.sciorder.client.android.common.repository.owner

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.android.common.model.remote.ApiResponse
import pl.karol202.sciorder.common.model.Owner

interface OwnerRepository
{
	fun getOwner(): LiveData<Owner?>

	fun login(name: String, hash: String?): LiveData<ApiResponse<Owner>>

	fun register(name: String, hash: String): LiveData<ApiResponse<Owner>>

	fun logout()
}
