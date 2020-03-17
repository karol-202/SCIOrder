package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.Order

interface OrderDao : InsertDao<Order>
{
	suspend fun updateStatus(orderId: Long, status: Order.Status)
	
	suspend fun deleteAll()

	suspend fun deleteByStoreId(storeId: Long)
	
	suspend fun dispatchByStoreId(storeId: Long, newOrders: List<Order>)
	
	fun getById(orderId: Long): Flow<Order?>
	
	fun getByStoreId(storeId: Long): Flow<List<Order>>
}
