package pl.karol202.sciorder.client.common.api.product.parameter

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductParameterRequest

interface ProductParameterApi
{
	suspend fun addParameter(token: String,
	                         storeId: Long,
	                         productId: Long,
	                         parameter: ProductParameterRequest): ApiResponse<ProductParameter>
	
	suspend fun updateParameter(token: String,
	                            storeId: Long,
	                            productId: Long,
	                            parameterId: Long,
	                            parameter: ProductParameterRequest): ApiResponse<Unit>
	
	suspend fun removeParameter(token: String,
	                            storeId: Long,
	                            productId: Long,
	                            parameterId: String): ApiResponse<Unit>
	
	suspend fun getParameters(token: String,
	                          storeId: Long,
	                          productId: Long): ApiResponse<List<ProductParameter>>
}
