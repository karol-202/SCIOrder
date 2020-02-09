package pl.karol202.sciorder.client.common.api.owner

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.Owner

interface OwnerApi
{
	suspend fun addOwner(owner: Owner): ApiResponse<Owner>

	suspend fun updateOwnerHash(ownerId: String, hash: String, newHash: String): ApiResponse<Unit>

	suspend fun getOwnerByName(name: String, hash: String? = null): ApiResponse<Owner>
}
