package pl.karol202.sciorder.client.common.database.order

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.database.CrudDao
import pl.karol202.sciorder.common.model.Order

interface OrderDao : CrudDao<Order>
{
	suspend fun updateStatus(id: String, status: Order.Status)

	suspend fun deleteAll()

	fun getByOwnerId(ownerId: String): Flow<List<Order>>
	
	fun getStatus(id: String): Flow<Order.Status?>
}
