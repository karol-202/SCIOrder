package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.model.Order

interface OrderApi
{
	fun addOrder(ownerId: String, order: Order): ApiResponse<Order>

	fun updateOrderStatus(ownerId: String, orderId: String, hash: String, status: Order.Status): ApiResponse<Unit>

	fun removeAllOrders(ownerId: String, hash: String): ApiResponse<Unit>

	fun getAllOrders(ownerId: String, hash: String): ApiResponse<List<Order>>

	fun getOrdersByIds(ownerId: String, ids: List<String>): ApiResponse<List<Order>>
}
