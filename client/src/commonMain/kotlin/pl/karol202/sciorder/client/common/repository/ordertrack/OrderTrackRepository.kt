package pl.karol202.sciorder.client.common.repository.ordertrack

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Owner

interface OrderTrackRepository
{
	fun getTrackedOrders(ownerId: String): Resource<List<Order>>

	suspend fun executeOrder(owner: Owner, order: Order): ApiResponse<Order>

	suspend fun removeOrder(order: Order)
}
