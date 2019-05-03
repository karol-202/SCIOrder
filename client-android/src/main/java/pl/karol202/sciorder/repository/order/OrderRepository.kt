package pl.karol202.sciorder.repository.order

import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.repository.Resource

interface OrderRepository
{
	fun getTrackedOrders(): Resource<List<Order>>
}
