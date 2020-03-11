package pl.karol202.sciorder.client.common.api.product.parameter

import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.authToken
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductParameterRequest

class KtorProductParameterApi(private val basicApi: KtorBasicApi) : ProductParameterApi
{
	override suspend fun addParameter(token: String,
	                                  storeId: Long,
	                                  productId: Long,
	                                  parameter: ProductParameterRequest) = basicApi.post<ProductParameter> {
		relativePath("api/stores/$storeId/products/$productId/parameters")
		authToken(token)
		jsonBody(parameter)
	}
	
	override suspend fun updateParameter(token: String,
	                                     storeId: Long,
	                                     productId: Long,
	                                     parameterId: Long,
	                                     parameter: ProductParameterRequest) = basicApi.put<Unit> {
		relativePath("api/stores/$storeId/products/$productId/parameters/$parameterId")
		authToken(token)
		jsonBody(parameter)
	}
	
	override suspend fun removeParameter(token: String,
	                                     storeId: Long,
	                                     productId: Long,
	                                     parameterId: String) = basicApi.delete<Unit> {
		relativePath("api/stores/$storeId/products/$productId/parameters/$parameterId")
		authToken(token)
	}
	
	override suspend fun getParameters(token: String,
	                                   storeId: Long,
	                                   productId: Long) = basicApi.get<List<ProductParameter>> {
		relativePath("api/stores/$storeId/products/$productId/parameters")
		authToken(token)
	}
}
