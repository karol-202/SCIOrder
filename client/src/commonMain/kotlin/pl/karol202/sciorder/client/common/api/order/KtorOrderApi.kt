package pl.karol202.sciorder.client.common.api.order

import io.ktor.client.request.parameter
import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.parameters
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.Order

class KtorOrderApi(private val basicApi: KtorBasicApi) : OrderApi
{
	override suspend fun addOrder(ownerId: String, order: Order) = basicApi.post<Order> {
		relativePath("owner/$ownerId/orders")
		jsonBody(order)
	}

	override suspend fun updateOrderStatus(ownerId: String,
	                                       orderId: String,
	                                       hash: String,
	                                       status: Order.Status) = basicApi.put<Unit> {
		relativePath("owner/$ownerId/orders/$orderId/status")
		parameter("hash", hash)
		parameter("status", status)
	}

	override suspend fun removeAllOrders(ownerId: String, hash: String) = basicApi.delete<Unit> {
		relativePath("owner/$ownerId/orders")
		parameter("hash", hash)
	}

	override suspend fun getAllOrders(ownerId: String, hash: String) = basicApi.get<List<Order>> {
		relativePath("owner/$ownerId/orders")
		parameter("all", true)
		parameter("hash", hash)
	}

	override suspend fun getOrdersByIds(ownerId: String, ids: List<String>) = basicApi.get<List<Order>> {
		relativePath("owner/$ownerId/orders")
		parameters("orderId", ids)
	}
}
