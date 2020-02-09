package pl.karol202.sciorder.client.common.repository.ordertrack

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Owner

interface OrderTrackRepository
{
	fun getTrackedOrdersResource(ownerId: String): Resource<List<Order>>

	suspend fun executeOrder(owner: Owner, order: Order): ApiResponse<Order>

	suspend fun removeOrder(order: Order)
}
