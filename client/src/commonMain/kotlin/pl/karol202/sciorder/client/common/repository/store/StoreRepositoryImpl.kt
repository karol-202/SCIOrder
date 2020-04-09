package pl.karol202.sciorder.client.common.repository.store

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.ApiResponse.Error.Type.LOCAL_INCONSISTENCY
import pl.karol202.sciorder.client.common.api.store.StoreApi
import pl.karol202.sciorder.client.common.database.dao.StoreDao
import pl.karol202.sciorder.client.common.database.dao.delete
import pl.karol202.sciorder.client.common.database.dao.insert
import pl.karol202.sciorder.client.common.repository.resource.StandardResource
import pl.karol202.sciorder.client.common.util.minutes
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest

class StoreRepositoryImpl(private val storeDao: StoreDao,
                          private val storeApi: StoreApi) : StoreRepository
{
	override fun getSelectedStoreFlow() = storeDao.getSelected().distinctUntilChanged()
	
	override fun getStoresResource(token: String) =
			StandardResource(updateIntervalMillis = 5.minutes,
			                 getFromApi = { storeApi.getStores(token) },
			                 getFromDB = { storeDao.getAll() },
			                 saveToDB = { storeDao.dispatch(it) })
	
	override suspend fun addStore(token: String, store: StoreRequest): ApiResponse<Store>
	{
		suspend fun saveLocally(store: Store) = storeDao.insert(store)
		
		return storeApi.addStore(token, store).ifSuccess { saveLocally(it) }
	}
	
	override suspend fun removeStore(token: String, storeId: Long): ApiResponse<Unit>
	{
		val removedStore = storeDao.getById(storeId).first() ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)
		
		suspend fun removeLocally() = storeDao.delete(removedStore)
		suspend fun revertLocally() = storeDao.insert(removedStore)
		
		removeLocally()
		return storeApi.removeStore(token, storeId).ifFailure { revertLocally() }
	}
	
	override suspend fun selectStore(storeId: Long?) = storeDao.updateSelection(storeId)
	
	override suspend fun cleanLocalStores() = storeDao.deleteAll()
}
