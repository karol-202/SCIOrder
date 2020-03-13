package pl.karol202.sciorder.client.common.repository.order

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest

interface OrderRepository
{
	fun getOrdersResource(token: String, storeId: Long): Resource<List<Order>>
	
	suspend fun addOrder(token: String, storeId: Long, order: OrderRequest): ApiResponse<Order>

	suspend fun updateOrderStatus(token: String, storeId: Long, orderId: Long, status: Order.Status): ApiResponse<Unit>

	suspend fun removeAllOrders(token: String, storeId: Long): ApiResponse<Unit>
	
	suspend fun cleanLocalOrders()
}
