package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.model.Owner

interface OwnerApi
{
	fun addOwner(owner: Owner): ApiResponse<Owner>

	fun updateOwnerHash(ownerId: String, hash: String, newHash: String): ApiResponse<Unit>

	fun getOwnerByName(name: String, hash: String? = null): ApiResponse<Owner>
}
