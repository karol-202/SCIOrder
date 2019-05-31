package pl.karol202.sciorder.client.admin.repository.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.MixedResource
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Owner

interface OrderRepository
{
	fun getAllOrders(ownerId: String, hash: String): MixedResource<List<Order>>

	fun updateOrderStatus(owner: Owner, order: Order, status: Order.Status): LiveData<ApiResponse<Unit>>

	fun removeAllOrders(owner: Owner): LiveData<ApiResponse<Unit>>
}
