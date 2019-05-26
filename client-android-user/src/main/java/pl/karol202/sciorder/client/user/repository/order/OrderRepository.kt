package pl.karol202.sciorder.client.user.repository.order

import pl.karol202.sciorder.client.common.repository.resource.MixedResource
import pl.karol202.sciorder.common.model.Order

interface OrderRepository
{
	fun getTrackedOrders(ownerId: String): MixedResource<List<Order>>
}
