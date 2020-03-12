package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.Order

interface OrderDao : CrudDao<Order>
{
	suspend fun updateStatus(orderId: Long, status: Order.Status)

	suspend fun deleteByStoreId(storeId: Long)
	
	fun getById(orderId: Long): Flow<Order?>
	
	fun getByStoreId(storeId: Long): Flow<List<Order>>
}
