package pl.karol202.sciorder.model.remote

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductApi
{
	companion object
	{
		fun create() = ApiBuilder.create<ProductApi>()
	}

	@GET("getProducts")
	fun getAllProducts(): LiveData<ApiResponse<List<Product>>>
}