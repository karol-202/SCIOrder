package pl.karol202.sciorder.client.admin.repository.order

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.common.repository.Resource

interface OrderRepository
{
	fun getAllOrders(): Resource<List<Order>>
}
