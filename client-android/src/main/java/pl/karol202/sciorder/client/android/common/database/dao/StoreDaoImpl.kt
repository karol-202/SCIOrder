package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.room.dao.StoreEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.dispatch
import pl.karol202.sciorder.client.android.common.database.room.entity.StoreEntity
import pl.karol202.sciorder.client.common.database.dao.StoreDao
import pl.karol202.sciorder.common.model.Store

class StoreDaoImpl(private val localDatabase: LocalDatabase,
                   private val storeEntityDao: StoreEntityDao) : StoreDao
{
	override suspend fun insert(items: List<Store>) = storeEntityDao.insert(items.toEntities())
	
	override suspend fun delete(items: List<Store>) = storeEntityDao.delete(items.toEntities())
	
	override suspend fun updateSelection(storeId: Long?) = storeEntityDao.updateSelection(storeId)
	
	override suspend fun deleteAll() = storeEntityDao.deleteAll()
	
	override suspend fun dispatch(newStores: List<Store>) = localDatabase.withTransaction {
		val oldStores = storeEntityDao.getAll().first()
		storeEntityDao.dispatch(oldStores, newStores.toEntities())
	}
	
	override fun getById(storeId: Long) = storeEntityDao.getById(storeId).map { it?.toModel() }
	
	override fun getSelected() = storeEntityDao.getSelected().map { it?.toModel() }
	
	override fun getAll() = storeEntityDao.getAll().map { it.toModels() }
	
	private fun Store.toEntity() = StoreEntity(id, name, false)
	
	private fun List<Store>.toEntities() = map { it.toEntity() }
	
	private fun StoreEntity.toModel() = Store(id, name)
	
	private fun List<StoreEntity>.toModels() = map { it.toModel() }
}
