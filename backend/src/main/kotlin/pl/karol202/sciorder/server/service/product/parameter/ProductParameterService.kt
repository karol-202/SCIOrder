package pl.karol202.sciorder.server.service.product.parameter

import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductParameterRequest

interface ProductParameterService
{
	suspend fun insertParameter(storeId: Long, productId: Long, parameter: ProductParameterRequest): ProductParameter
	
	suspend fun updateParameter(storeId: Long, productId: Long, parameterId: Long, parameter: ProductParameterRequest)
	
	suspend fun deleteParameter(storeId: Long, productId: Long, parameterId: Long)
	
	suspend fun getParameters(storeId: Long, productId: Long): List<ProductParameter>
}
