package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntryEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntryParameterValueEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.dispatch
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryParameterValueEntity
import pl.karol202.sciorder.client.common.database.dao.OrderDao
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.OrderEntry
import pl.karol202.sciorder.common.util.map

class OrderDaoImpl(private val localDatabase: LocalDatabase,
                   private val orderEntityDao: OrderEntityDao,
                   private val orderEntryEntityDao: OrderEntryEntityDao,
                   private val orderEntryParameterValueEntityDao: OrderEntryParameterValueEntityDao) : OrderDao
{
	override suspend fun insert(items: List<Order>) = localDatabase.withTransaction {
		val orderEntities = items.map { it.toEntity() }
		orderEntityDao.insert(orderEntities)
		
		val entryEntities = items.flatMap { it.entries }.map { it.toEntity() }
		orderEntryEntityDao.insert(entryEntities)
		
		val parameterEntities = items.flatMap { it.entries }.flatMap { it.parameters.toEntities(it.id) }
		orderEntryParameterValueEntityDao.insert(parameterEntities)
	}
	
	override suspend fun updateStatus(orderId: Long, status: Order.Status) = orderEntityDao.updateStatus(orderId, status)
	
	override suspend fun deleteAll() = orderEntityDao.deleteAll()
	
	override suspend fun deleteByStoreId(storeId: Long) = orderEntityDao.deleteByStoreId(storeId)
	
	override suspend fun dispatchByStoreId(storeId: Long, newOrders: List<Order>) = localDatabase.withTransaction {
		val oldData = orderEntityDao.getByStoreId(storeId).first()
		
		val oldOrderEntities = oldData.map { it.order }
		val newOrderEntities = newOrders.map { it.toEntity() }
		orderEntityDao.dispatch(oldOrderEntities, newOrderEntities)
		
		val oldEntryEntities = oldData.flatMap { it.entries }.map { it.orderEntry }
		val newEntryEntities = newOrders.flatMap { it.entries }.map { it.toEntity() }
		orderEntryEntityDao.dispatch(oldEntryEntities, newEntryEntities)
		
		val oldParameterEntities = oldData.flatMap { it.entries }.flatMap { it.parameters }
		val newParameterEntities = newOrders.flatMap { it.entries }.flatMap { it.parameters.toEntities(it.id) }
		orderEntryParameterValueEntityDao.delete(oldParameterEntities)
		orderEntryParameterValueEntityDao.insert(newParameterEntities)
	}
	
	override fun getById(orderId: Long) = orderEntityDao.getById(orderId).map { it?.map() }
	
	override fun getByStoreId(storeId: Long) = orderEntityDao.getByStoreId(storeId).map { it.map() }
	
	private fun Order.toEntity() = OrderEntity(id, storeId, details, status)
	
	private fun OrderEntry.toEntity() = OrderEntryEntity(id, orderId, productId, quantity)
	
	private fun Map<Long, String>.toEntities(orderEntryId: Long) = map { (productParameterId, value) ->
		OrderEntryParameterValueEntity(0, orderEntryId, productParameterId, value)
	}
}
