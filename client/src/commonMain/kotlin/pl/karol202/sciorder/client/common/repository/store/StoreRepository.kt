package pl.karol202.sciorder.client.common.repository.store

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest

interface StoreRepository
{
	fun getSelectedStoreFlow(): Flow<Store?>
	
	fun getStoresResource(token: String): Resource<List<Store>>
	
	suspend fun addStore(token: String, store: StoreRequest): ApiResponse<Store>
	
	suspend fun removeStore(token: String, storeId: Long): ApiResponse<Unit>
	
	suspend fun cleanLocalStores()
}
