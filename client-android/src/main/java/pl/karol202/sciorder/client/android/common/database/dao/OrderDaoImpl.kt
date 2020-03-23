package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntryEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntryParameterValueEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.dispatch
import pl.karol202.sciorder.client.android.common.database.room.relations.OrderWithEntries
import pl.karol202.sciorder.client.android.common.database.room.relations.entries
import pl.karol202.sciorder.client.android.common.database.room.relations.orders
import pl.karol202.sciorder.client.android.common.database.room.relations.parameterValues
import pl.karol202.sciorder.client.android.common.database.room.toEntities
import pl.karol202.sciorder.client.android.common.database.room.toModel
import pl.karol202.sciorder.client.android.common.database.room.toModels
import pl.karol202.sciorder.client.common.database.dao.OrderDao
import pl.karol202.sciorder.common.model.Order

class OrderDaoImpl(private val localDatabase: LocalDatabase,
                   private val orderEntityDao: OrderEntityDao,
                   private val orderEntryEntityDao: OrderEntryEntityDao,
                   private val orderEntryParameterValueEntityDao: OrderEntryParameterValueEntityDao) : OrderDao
{
	override suspend fun insert(items: List<Order>) = localDatabase.withTransaction {
		val entities = items.toEntities(OrderWithEntries)
		
		orderEntityDao.insert(entities.orders)
		orderEntryEntityDao.insert(entities.entries)
		orderEntryParameterValueEntityDao.insert(entities.parameterValues)
	}
	
	override suspend fun updateStatus(orderId: Long, status: Order.Status) = orderEntityDao.updateStatus(orderId, status)
	
	override suspend fun deleteAll() = orderEntityDao.deleteAll()
	
	override suspend fun deleteByStoreId(storeId: Long) = orderEntityDao.deleteByStoreId(storeId)
	
	override suspend fun dispatchByStoreId(storeId: Long, newOrders: List<Order>) = localDatabase.withTransaction {
		val oldEntities = orderEntityDao.getByStoreId(storeId).first()
		val newEntities = newOrders.toEntities(OrderWithEntries)
		
		orderEntityDao.dispatch(oldEntities.orders, newEntities.orders)
		orderEntryEntityDao.dispatch(oldEntities.entries, oldEntities.entries)
		orderEntryParameterValueEntityDao.dispatch(oldEntities.parameterValues, newEntities.parameterValues)
	}
	
	override fun getById(orderId: Long) = orderEntityDao.getById(orderId).map { it?.toModel(OrderWithEntries) }
	
	override fun getByStoreId(storeId: Long) = orderEntityDao.getByStoreId(storeId).map { it.toModels(OrderWithEntries) }
}
