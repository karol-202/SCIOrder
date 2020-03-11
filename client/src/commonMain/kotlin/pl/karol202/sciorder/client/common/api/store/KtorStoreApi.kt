package pl.karol202.sciorder.client.common.api.store

import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.authToken
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest

class KtorStoreApi(private val basicApi: KtorBasicApi) : StoreApi
{
	override suspend fun addStore(token: String, storeRequest: StoreRequest) = basicApi.post<Store> {
		relativePath("api/stores")
		authToken(token)
		jsonBody(storeRequest)
	}
	
	override suspend fun removeStore(token: String, storeId: Long) = basicApi.delete<Unit> {
		relativePath("api/stores/$storeId")
		authToken(token)
	}
	
	override suspend fun getStores(token: String) = basicApi.get<List<Store>> {
		relativePath("api/stores")
		authToken(token)
	}
}
