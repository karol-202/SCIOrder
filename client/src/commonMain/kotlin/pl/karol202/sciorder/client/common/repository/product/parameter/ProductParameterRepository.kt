package pl.karol202.sciorder.client.common.repository.product.parameter

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductParameterRequest

interface ProductParameterRepository
{
	fun getParametersResource(token: String, storeId: Long, productId: Long): Resource<List<ProductParameter>>
	
	suspend fun addParameter(token: String,
	                         storeId: Long,
	                         productId: Long,
	                         parameter: ProductParameterRequest): ApiResponse<ProductParameter>
	
	suspend fun updateParameter(token: String,
	                            storeId: Long,
	                            productId: Long,
	                            parameterId: Long,
	                            parameter: ProductParameterRequest): ApiResponse<Unit>
	
	suspend fun removeParameter(token: String, storeId: Long, productId: Long, parameterId: Long): ApiResponse<Unit>
	
	suspend fun cleanLocalParameters()
}
