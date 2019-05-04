package pl.karol202.sciorder.client.common.repository.order

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.common.repository.Resource

interface OrderRepository
{
	fun getTrackedOrders(): Resource<List<Order>>
}
