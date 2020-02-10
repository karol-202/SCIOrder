package pl.karol202.sciorder.client.common.repository.order

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Order

interface OrderRepository
{
	fun getOrdersResource(ownerId: String, hash: String): Resource<List<Order>>

	suspend fun updateOrderStatus(owner: Owner, order: Order, status: Order.Status): ApiResponse<Unit>

	suspend fun removeAllOrders(owner: Owner): ApiResponse<Unit>
}
