package pl.karol202.sciorder.client.admin.repository.order

import pl.karol202.sciorder.client.common.repository.resource.MixedResource
import pl.karol202.sciorder.common.model.Order

interface OrderRepository
{
	fun getAllOrders(ownerId: String): MixedResource<List<Order>>
}
