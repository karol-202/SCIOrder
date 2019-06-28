package pl.karol202.sciorder.client.common.model.remote.order

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import pl.karol202.sciorder.client.common.model.remote.BasicApi
import pl.karol202.sciorder.common.Order

class KtorOrderApi(httpClient: HttpClient,
                   serverUrl: String) : BasicApi(httpClient, serverUrl), OrderApi
{
	override suspend fun addOrder(ownerId: String, order: Order) = apiRequest<Order> {
		method = HttpMethod.Post
		apiUrl("owner/$ownerId/orders")
		json()
		body = order
	}

	override suspend fun updateOrderStatus(ownerId: String,
	                                       orderId: String,
	                                       hash: String,
	                                       status: Order.Status) = apiRequest<Unit> {
		method = HttpMethod.Put
		apiUrl("owner/$ownerId/orders/$orderId/status")
		parameter("hash", hash)
		parameter("status", status)
	}

	override suspend fun removeAllOrders(ownerId: String, hash: String) = apiRequest<Unit> {
		method = HttpMethod.Delete
		apiUrl("owner/$ownerId/orders")
		parameter("hash", hash)
	}

	override suspend fun getAllOrders(ownerId: String, hash: String) = apiRequest<List<Order>> {
		method = HttpMethod.Get
		apiUrl("owner/$ownerId/orders")
		parameter("all", true)
		parameter("hash", hash)
	}

	override suspend fun getOrdersByIds(ownerId: String, ids: List<String>) = apiRequest<List<Order>> {
		method = HttpMethod.Get
		apiUrl("owner/$ownerId/orders")
		parameters("orderId", ids)
	}
}
