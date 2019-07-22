package pl.karol202.sciorder.client.common.repository.owner

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.common.Owner

interface OwnerRepository
{
	fun getOwnerFlow(): Flow<Owner?>

	suspend fun login(name: String, hash: String?): ApiResponse<Owner>

	suspend fun register(owner: Owner): ApiResponse<Owner>

	suspend fun logout()
}
