package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.Store

interface StoreDao : CrudDao<Store>
{
	suspend fun deleteAll()
	
	fun getById(storeId: Long): Flow<Store?>
	
	fun getAll(): Flow<List<Store>>
}
