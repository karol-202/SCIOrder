package pl.karol202.sciorder.model.remote.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.remote.ApiResponse
import retrofit2.http.*

interface OrderApi
{
	@POST("orders")
	fun addOrder(@Body order: Order): LiveData<ApiResponse<Unit>>

	@GET("orders")
	fun getOrders(@Query("id") ids: List<String>): LiveData<ApiResponse<List<Order>>>
}
