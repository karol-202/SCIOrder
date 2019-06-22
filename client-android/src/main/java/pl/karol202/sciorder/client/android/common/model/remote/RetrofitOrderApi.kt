package pl.karol202.sciorder.client.android.common.model.remote

import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.common.Order
import retrofit2.http.*

interface RetrofitOrderApi
{
	@POST("owner/{ownerId}/orders")
	suspend fun addOrder(@Path("ownerId") ownerId: String,
	                     @Body order: Order): ApiResponse<Order>

	@PUT("owner/{ownerId}/orders/{orderId}/status")
	suspend fun updateOrderStatus(@Path("ownerId") ownerId: String,
	                              @Path("orderId") orderId: String,
	                              @Query("hash") hash: String,
	                              @Query("status") status: Order.Status): ApiResponse<Unit>

	@DELETE("owner/{ownerId}/orders")
	suspend fun removeAllOrders(@Path("ownerId") ownerId: String,
	                            @Query("hash") hash: String): ApiResponse<Unit>

	@GET("owner/{ownerId}/orders?all=true")
	suspend fun getAllOrders(@Path("ownerId") ownerId: String,
	                         @Query("hash") hash: String): ApiResponse<List<Order>>

	@GET("owner/{ownerId}/orders")
	suspend fun getOrdersByIds(@Path("ownerId") ownerId: String,
	                           @Query("orderId") ids: List<String>): ApiResponse<List<Order>>
}

// RetrofitOrderApi cannot extend OrderApi thanks to Jake Wharton
fun RetrofitOrderApi.asOrderApi() = object : OrderApi {
	override suspend fun addOrder(ownerId: String, order: Order) =
			this@asOrderApi.addOrder(ownerId, order)

	override suspend fun updateOrderStatus(ownerId: String,
	                                       orderId: String,
	                                       hash: String,
	                                       status: Order.Status) =
			this@asOrderApi.updateOrderStatus(ownerId, orderId, hash, status)

	override suspend fun removeAllOrders(ownerId: String, hash: String) =
			this@asOrderApi.removeAllOrders(ownerId, hash)

	override suspend fun getAllOrders(ownerId: String, hash: String) =
			this@asOrderApi.getAllOrders(ownerId, hash)

	override suspend fun getOrdersByIds(ownerId: String, ids: List<String>) =
			this@asOrderApi.getOrdersByIds(ownerId, ids)
}
