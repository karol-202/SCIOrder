package pl.karol202.sciorder.client.common.model.remote.owner

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import pl.karol202.sciorder.client.common.model.remote.BasicApi
import pl.karol202.sciorder.common.Owner

class KtorOwnerApi(httpClient: HttpClient,
                   serverUrl: String) : BasicApi(httpClient, serverUrl), OwnerApi
{
	override suspend fun addOwner(owner: Owner) = post<Owner> {
		apiUrl("owner")
		jsonBody(owner)
	}

	override suspend fun updateOwnerHash(ownerId: String, hash: String, newHash: String) = put<Unit> {
		apiUrl("owner/$ownerId/hash")
		parameter("hash", hash)
		parameter("newHash", newHash)
	}

	override suspend fun getOwnerByName(name: String, hash: String?) = get<Owner> {
		apiUrl("owner")
		parameter("name", name)
		parameter("hash", hash)
	}
}
