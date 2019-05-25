package pl.karol202.sciorder.client.common.model.local.order

import pl.karol202.sciorder.client.common.model.local.CrudDao
import pl.karol202.sciorder.common.model.Order

interface OrderDao : CrudDao<Order>
{
	fun updateStatus(id: String, status: Order.Status)

	fun deleteAll()
}
