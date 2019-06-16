package pl.karol202.sciorder.client.common.model.local

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.Order

interface OrderDao : CrudDao<Order>
{
	suspend fun updateStatus(id: String, status: Order.Status)

	suspend fun deleteAll()

	fun getByOwnerId(ownerId: String): Flow<List<Order>>
}
