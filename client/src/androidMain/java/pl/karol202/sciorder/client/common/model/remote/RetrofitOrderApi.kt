package pl.karol202.sciorder.client.common.model.remote

import pl.karol202.sciorder.common.Order
import retrofit2.http.*

interface RetrofitOrderApi : OrderApi
{
	@POST("owner/{ownerId}/orders")
	override suspend fun addOrder(@Path("ownerId") ownerId: String,
	                              @Body order: Order): ApiResponse<Order>

	@PUT("owner/{ownerId}/orders/{orderId}/status")
	override suspend fun updateOrderStatus(@Path("ownerId") ownerId: String,
	                                       @Path("orderId") orderId: String,
	                                       @Query("hash") hash: String,
	                                       @Query("status") status: Order.Status): ApiResponse<Unit>

	@DELETE("owner/{ownerId}/orders")
	override suspend fun removeAllOrders(@Path("ownerId") ownerId: String,
	                                     @Query("hash") hash: String): ApiResponse<Unit>

	@GET("owner/{ownerId}/orders?all=true")
	override suspend fun getAllOrders(@Path("ownerId") ownerId: String,
	                                  @Query("hash") hash: String): ApiResponse<List<Order>>

	@GET("owner/{ownerId}/orders")
	override suspend fun getOrdersByIds(@Path("ownerId") ownerId: String,
	                                    @Query("orderId") ids: List<String>): ApiResponse<List<Order>>
}
