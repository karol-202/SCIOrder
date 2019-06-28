package pl.karol202.sciorder.client.common.model.remote.order

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.common.Order

interface OrderApi
{
	suspend fun addOrder(ownerId: String, order: Order): ApiResponse<Order>

	suspend fun updateOrderStatus(ownerId: String, orderId: String, hash: String, status: Order.Status): ApiResponse<Unit>

	suspend fun removeAllOrders(ownerId: String, hash: String): ApiResponse<Unit>

	suspend fun getAllOrders(ownerId: String, hash: String): ApiResponse<List<Order>>

	suspend fun getOrdersByIds(ownerId: String, ids: List<String>): ApiResponse<List<Order>>
}
