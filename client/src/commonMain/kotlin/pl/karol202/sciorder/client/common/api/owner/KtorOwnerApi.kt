package pl.karol202.sciorder.client.common.api.owner

import io.ktor.client.request.parameter
import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath

class KtorOwnerApi(private val basicApi: KtorBasicApi) : OwnerApi
{
	override suspend fun addOwner(owner: Owner) = basicApi.post<Owner> {
		relativePath("owner")
		jsonBody(owner)
	}

	override suspend fun updateOwnerHash(ownerId: String, hash: String, newHash: String) = basicApi.put<Unit> {
		relativePath("owner/$ownerId/hash")
		parameter("hash", hash)
		parameter("newHash", newHash)
	}

	override suspend fun getOwnerByName(name: String, hash: String?) = basicApi.get<Owner> {
		relativePath("owner")
		parameter("name", name)
		parameter("hash", hash)
	}
}
