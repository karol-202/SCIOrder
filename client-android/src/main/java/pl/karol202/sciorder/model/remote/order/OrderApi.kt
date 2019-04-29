package pl.karol202.sciorder.model.remote.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.remote.ApiBuilder
import pl.karol202.sciorder.model.remote.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApi
{
	companion object
	{
		fun create() = ApiBuilder.create<OrderApi>()
	}

	@POST("addOrder")
	fun addOrder(@Body order: Order): LiveData<ApiResponse<Unit>>
}