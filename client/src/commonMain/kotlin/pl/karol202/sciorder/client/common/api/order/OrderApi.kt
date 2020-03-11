package pl.karol202.sciorder.client.common.api.order

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest

interface OrderApi
{
	suspend fun addOrder(token: String, storeId: Long, order: OrderRequest): ApiResponse<Order>

	suspend fun updateOrderStatus(token: String, storeId: Long, orderId: Long, status: Order.Status): ApiResponse<Unit>

	suspend fun removeAllOrders(token: String, storeId: Long): ApiResponse<Unit>

	suspend fun getOrders(token: String, storeId: Long): ApiResponse<List<Order>>
}
