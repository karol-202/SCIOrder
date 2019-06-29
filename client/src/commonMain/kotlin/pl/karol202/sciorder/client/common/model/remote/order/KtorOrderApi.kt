package pl.karol202.sciorder.client.common.model.remote.order

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import pl.karol202.sciorder.client.common.model.remote.BasicApi
import pl.karol202.sciorder.common.Order

class KtorOrderApi(httpClient: HttpClient,
                   serverUrl: String) : BasicApi(httpClient, serverUrl), OrderApi
{
	override suspend fun addOrder(ownerId: String, order: Order) = post<Order> {
		apiUrl("owner/$ownerId/orders")
		jsonBody(order)
	}

	override suspend fun updateOrderStatus(ownerId: String,
	                                       orderId: String,
	                                       hash: String,
	                                       status: Order.Status) = put<Unit> {
		apiUrl("owner/$ownerId/orders/$orderId/status")
		parameter("hash", hash)
		parameter("status", status)
	}

	override suspend fun removeAllOrders(ownerId: String, hash: String) = delete<Unit> {
		apiUrl("owner/$ownerId/orders")
		parameter("hash", hash)
	}

	override suspend fun getAllOrders(ownerId: String, hash: String) = get<String> {
		apiUrl("owner/$ownerId/orders")
		parameter("all", true)
		parameter("hash", hash)
	}.map { Json.parse(Order.serializer().list, it) } // Workaround for Ktor's KotlinxSerializer not being able to deserialize lists

	override suspend fun getOrdersByIds(ownerId: String, ids: List<String>) = get<String> {
		apiUrl("owner/$ownerId/orders")
		parameters("orderId", ids)
	}.map { Json.parse(Order.serializer().list, it) } // Workaround for Ktor's KotlinxSerializer not being able to deserialize lists
}
