package pl.karol202.sciorder.client.common.model.remote.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import retrofit2.http.GET

interface ProductApi
{
	@GET("products")
	fun getAllProducts(): LiveData<ApiResponse<List<Product>>>
}
