package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.Store

interface StoreDao : InsertDao<Store>, DeleteDao<Store>
{
	suspend fun updateSelection(storeId: Long?)
	
	suspend fun deleteAll()
	
	suspend fun dispatch(newStores: List<Store>)
	
	fun getById(storeId: Long): Flow<Store?>
	
	fun getSelected(): Flow<Store?>
	
	fun getAll(): Flow<List<Store>>
}
