package pl.karol202.sciorder.client.user.model.remote.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.user.model.remote.ApiResponse
import retrofit2.http.*

interface OrderApi
{
	@POST("orders")
	fun addOrder(@Body order: Order): LiveData<ApiResponse<Order>>

	@GET("ordersById")
	fun getOrdersById(@Query("id") ids: List<String>): LiveData<ApiResponse<List<Order>>>
}
