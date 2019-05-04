package pl.karol202.sciorder.client.common.model.remote.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.http.*

interface OrderApi
{
	@PUT("orders")
	fun addOrder(@Body order: Order): LiveData<ApiResponse<Order>>

	@POST("orders/{id}/status")
	fun updateOrderStatus(@Path("id") id: String, @Query("status") status: Order.Status): LiveData<ApiResponse<Unit>>

	@GET("orders?all=true")
	fun getAllOrders(): LiveData<ApiResponse<List<Order>>>

	@GET("orders")
	fun getOrdersById(@Query("id") ids: List<String>): LiveData<ApiResponse<List<Order>>>
}
