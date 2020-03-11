package pl.karol202.sciorder.client.common.api.store

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest

interface StoreApi
{
	suspend fun addStore(token: String, storeRequest: StoreRequest): ApiResponse<Store>
	
	suspend fun removeStore(token: String, storeId: Long): ApiResponse<Unit>
	
	suspend fun getStores(token: String): ApiResponse<List<Store>>
}
