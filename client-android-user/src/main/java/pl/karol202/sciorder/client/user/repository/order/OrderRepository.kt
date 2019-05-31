package pl.karol202.sciorder.client.user.repository.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.MixedResource
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Owner

interface OrderRepository
{
	fun getTrackedOrders(ownerId: String): MixedResource<List<Order>>

	fun executeOrder(owner: Owner, order: Order): LiveData<ApiResponse<Order>>
}
