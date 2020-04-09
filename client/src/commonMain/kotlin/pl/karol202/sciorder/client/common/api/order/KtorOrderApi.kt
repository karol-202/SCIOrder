package pl.karol202.sciorder.client.common.api.order

import io.ktor.client.request.parameter
import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.authToken
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest

class KtorOrderApi(private val basicApi: KtorBasicApi) : OrderApi
{
	override suspend fun addOrder(token: String, storeId: Long, order: OrderRequest) = basicApi.post<Order> {
		relativePath("api/stores/$storeId/orders")
		jsonBody(order)
	}

	override suspend fun updateOrderStatus(token: String,
	                                       storeId: Long,
	                                       orderId: Long,
	                                       status: Order.Status) = basicApi.put<Unit> {
		relativePath("api/stores/$storeId/orders/$orderId/status")
		authToken(token)
		parameter("status", status)
	}

	override suspend fun removeAllOrders(token: String, storeId: Long) = basicApi.delete<Unit> {
		relativePath("api/stores/$storeId/orders")
		authToken(token)
	}

	override suspend fun getOrders(token: String, storeId: Long) = basicApi.get<List<Order>> {
		relativePath("api/stores/$storeId/orders")
		authToken(token)
	}
}
