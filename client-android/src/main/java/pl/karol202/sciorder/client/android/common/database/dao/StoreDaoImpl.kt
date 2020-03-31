package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.room.dao.*
import pl.karol202.sciorder.client.android.common.database.room.entity.StoreEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.*
import pl.karol202.sciorder.client.android.common.database.room.toEntities
import pl.karol202.sciorder.client.android.common.database.room.toModel
import pl.karol202.sciorder.client.android.common.database.room.toModels
import pl.karol202.sciorder.client.common.database.dao.StoreDao
import pl.karol202.sciorder.common.model.Store

class StoreDaoImpl(private val localDatabase: LocalDatabase,
                   private val storeEntityDao: StoreEntityDao,
                   private val productEntityDao: ProductEntityDao,
                   private val productParameterEntityDao: ProductParameterEntityDao,
                   private val productParameterEnumValueEntityDao: ProductParameterEnumValueEntityDao,
                   private val orderEntityDao: OrderEntityDao,
                   private val orderEntryEntityDao: OrderEntryEntityDao,
                   private val orderEntryParameterValueEntityDao: OrderEntryParameterValueEntityDao) : StoreDao
{
	override suspend fun insert(items: List<Store>) = localDatabase.withTransaction {
		val entities = items.toEntities(StoreWithProductsAndOrders)
		
		storeEntityDao.insert(entities.stores)
		productEntityDao.insert(entities.products)
		productParameterEntityDao.insert(entities.productsParameters)
		productParameterEnumValueEntityDao.insert(entities.productsEnumValues)
		orderEntityDao.insert(entities.orders)
		orderEntryEntityDao.insert(entities.ordersEntries)
		orderEntryParameterValueEntityDao.insert(entities.ordersParameterValues)
	}
	
	override suspend fun delete(items: List<Store>) = storeEntityDao.delete(items.toEntities(StoreEntity))
	
	override suspend fun updateSelection(storeId: Long?) = storeEntityDao.updateSelection(storeId)
	
	override suspend fun deleteAll() = storeEntityDao.deleteAll()
	
	override suspend fun dispatch(newStores: List<Store>) = localDatabase.withTransaction {
		val oldEntities = storeEntityDao.getAll().first()
		val newEntities = newStores.toEntities(StoreWithProductsAndOrders)
		
		storeEntityDao.dispatch(oldEntities.stores, newEntities.stores)
		productEntityDao.dispatch(oldEntities.products, newEntities.products)
		productParameterEntityDao.dispatch(oldEntities.productsParameters, newEntities.productsParameters)
		productParameterEnumValueEntityDao.dispatch(oldEntities.productsEnumValues, newEntities.productsEnumValues)
		orderEntityDao.dispatch(oldEntities.orders, newEntities.orders)
		orderEntryEntityDao.dispatch(oldEntities.ordersEntries, newEntities.ordersEntries)
		orderEntryParameterValueEntityDao.dispatch(oldEntities.ordersParameterValues, newEntities.ordersParameterValues)
	}
	
	override fun getById(storeId: Long) = storeEntityDao.getById(storeId).map { it?.toModel(StoreWithProductsAndOrders) }
	
	override fun getSelected() = storeEntityDao.getSelected().map { it?.toModel(StoreWithProductsAndOrders) }
	
	override fun getAll() = storeEntityDao.getAll().map { it.toModels(StoreWithProductsAndOrders) }
}
