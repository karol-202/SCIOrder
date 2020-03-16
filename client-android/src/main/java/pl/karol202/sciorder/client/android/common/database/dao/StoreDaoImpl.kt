package pl.karol202.sciorder.client.android.common.database.dao

import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.dao.StoreEntityDao
import pl.karol202.sciorder.client.android.common.database.room.entity.StoreEntity
import pl.karol202.sciorder.client.android.common.util.toEntity
import pl.karol202.sciorder.client.android.common.util.toModel
import pl.karol202.sciorder.client.common.database.dao.StoreDao
import pl.karol202.sciorder.common.model.Store

class StoreDaoImpl(private val storeEntityDao: StoreEntityDao) : StoreDao
{
	override suspend fun insert(items: List<Store>) = storeEntityDao.insert(items.toEntity(StoreEntity))
	
	override suspend fun update(items: List<Store>) = storeEntityDao.update(items.toEntity(StoreEntity))
	
	override suspend fun delete(items: List<Store>) = storeEntityDao.delete(items.toEntity(StoreEntity))
	
	override suspend fun updateSelection(storeId: Long?) = storeEntityDao.updateSelection(storeId)
	
	override suspend fun deleteAll() = storeEntityDao.deleteAll()
	
	override fun getById(storeId: Long) = storeEntityDao.getById(storeId).map { it.toModel(StoreEntity) }
	
	override fun getSelected() = storeEntityDao.getSelected().map { it.toModel(StoreEntity) }
	
	override fun getAll() = storeEntityDao.getAll().map { it.toModel(StoreEntity) }
}
