package pl.karol202.sciorder.client.common.api.product

import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.authToken
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest

class KtorProductApi(private val basicApi: KtorBasicApi) : ProductApi
{
	override suspend fun addProduct(token: String, storeId: Long, product: ProductCreateRequest) = basicApi.post<Product> {
		relativePath("api/stores/$storeId/products")
		authToken(token)
		jsonBody(product)
	}

	override suspend fun updateProduct(token: String,
	                                   storeId: Long,
	                                   productId: Long,
	                                   product: ProductUpdateRequest) = basicApi.put<Unit> {
		relativePath("api/stores/$storeId/products/$productId")
		authToken(token)
		jsonBody(product)
	}

	override suspend fun removeProduct(token: String, storeId: Long, productId: Long) = basicApi.delete<Unit> {
		relativePath("api/stores/$storeId/products/$productId")
		authToken(token)
	}

	override suspend fun getProducts(token: String, storeId: Long) = basicApi.get<List<Product>> {
		relativePath("api/stores/$storeId/products")
		authToken(token)
	}
}
