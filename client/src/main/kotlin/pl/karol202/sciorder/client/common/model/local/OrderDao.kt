package pl.karol202.sciorder.client.common.model.local

import pl.karol202.sciorder.common.model.Order

interface OrderDao : CrudDao<Order>
{
	suspend fun updateStatus(id: String, status: Order.Status)

	suspend fun deleteAll()

	suspend fun getByOwnerId(ownerId: String): List<Order>
}
