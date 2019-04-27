package pl.karol202.sciorder.model.remote.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.model.remote.ApiBuilder
import pl.karol202.sciorder.model.remote.ApiResponse
import retrofit2.http.GET

interface ProductApi
{
	companion object
	{
		fun create() = ApiBuilder.create<ProductApi>()
	}

	@GET("getProducts")
	fun getAllProducts(): LiveData<ApiResponse<List<Product>>>
}