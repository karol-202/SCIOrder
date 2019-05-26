package pl.karol202.sciorder.client.common.model.remote

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.http.*

interface OrderApi
{
	@POST("owner/{ownerId}/orders")
	fun addOrder(@Path("ownerId") ownerId: String,
	             @Body order: Order):
			LiveData<ApiResponse<Order>>

	@PUT("owner/{ownerId}/orders/{orderId}/status")
	fun updateOrderStatus(@Path("ownerId") ownerId: String,
	                      @Path("orderId") orderId: String,
	                      @Query("status") status: Order.Status):
			LiveData<ApiResponse<Unit>>

	@DELETE("owner/{ownerId}/orders")
	fun removeAllOrders(@Path("ownerId") ownerId: String):
			LiveData<ApiResponse<Unit>>

	@GET("owner/{ownerId}/orders?all=true")
	fun getAllOrders(@Path("ownerId") ownerId: String):
			LiveData<ApiResponse<List<Order>>>

	@GET("owner/{ownerId}/orders")
	fun getOrdersById(@Path("ownerId") ownerId: String,
	                  @Query("orderId") ids: List<String>):
			LiveData<ApiResponse<List<Order>>>
}
