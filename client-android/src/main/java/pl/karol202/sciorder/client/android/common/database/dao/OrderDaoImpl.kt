package pl.karol202.sciorder.client.android.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntryEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.OrderEntryParameterValueEntityDao
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryParameterValueEntity
import pl.karol202.sciorder.client.android.common.util.toEntity
import pl.karol202.sciorder.client.common.database.dao.OrderDao
import pl.karol202.sciorder.client.common.database.dao.dispatchDiff
import pl.karol202.sciorder.common.model.Order

class OrderDaoImpl(private val orderEntityDao: OrderEntityDao,
                   private val orderEntryEntityDao: OrderEntryEntityDao,
                   private val orderEntryParameterValueEntityDao: OrderEntryParameterValueEntityDao) : OrderDao
{
	override suspend fun insert(items: List<Order>)
	{
		orderEntityDao.insert(items.toEntity(OrderEntity))
		
		val entries = items.flatMap { it.entries }
		orderEntryEntityDao.insert(entries.toEntity(OrderEntryEntity))
		
		val parametersValues = entries.flatMap { entry ->
			entry.parameters.map { (parameterId, value) ->
				OrderEntryParameterValueEntity(0, entry.id, parameterId, value)
			}
		}
		orderEntryParameterValueEntityDao.insert(parametersValues)
	}
	
	override suspend fun update(items: List<Order>)
	{
		orderEntityDao.update(items.toEntity(OrderEntity))
		
		orderEntryEntityDao.dispatchDiff()
	}
	
	override suspend fun delete(items: List<Order>) = orderEntityDao.delete(items.toEntity(OrderEntity))
	
	override suspend fun updateStatus(orderId: Long, status: Order.Status)
	{
		TODO("Not yet implemented")
	}
	
	override suspend fun deleteAll()
	{
		TODO("Not yet implemented")
	}
	
	override suspend fun deleteByStoreId(storeId: Long)
	{
		TODO("Not yet implemented")
	}
	
	override fun getById(orderId: Long): Flow<Order?>
	{
		TODO("Not yet implemented")
	}
	
	override fun getByStoreId(storeId: Long): Flow<List<Order>>
	{
		TODO("Not yet implemented")
	}
}
