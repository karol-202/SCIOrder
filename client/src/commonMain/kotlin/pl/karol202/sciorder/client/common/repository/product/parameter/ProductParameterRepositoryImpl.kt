package pl.karol202.sciorder.client.common.repository.product.parameter

import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.ApiResponse.Error.Type.LOCAL_INCONSISTENCY
import pl.karol202.sciorder.client.common.api.product.parameter.ProductParameterApi
import pl.karol202.sciorder.client.common.database.dao.ProductParameterDao
import pl.karol202.sciorder.client.common.database.dao.delete
import pl.karol202.sciorder.client.common.database.dao.insert
import pl.karol202.sciorder.client.common.database.dao.update
import pl.karol202.sciorder.client.common.repository.resource.StandardMixedResource
import pl.karol202.sciorder.client.common.util.minutes
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductParameterRequest

class ProductParameterRepositoryImpl(private val productParameterDao: ProductParameterDao,
                                     private val productParameterApi: ProductParameterApi) : ProductParameterRepository
{
	override fun getParametersResource(token: String, storeId: Long, productId: Long) =
			StandardMixedResource(dao = productParameterDao,
			                      databaseProvider = { getByProductId(productId) },
			                      apiProvider = { productParameterApi.getParameters(token, storeId, productId) },
			                      updateIntervalMillis = 5.minutes)
	
	override suspend fun addParameter(token: String,
	                                  storeId: Long,
	                                  productId: Long,
	                                  parameter: ProductParameterRequest): ApiResponse<ProductParameter>
	{
		suspend fun saveLocally(parameter: ProductParameter) = productParameterDao.insert(parameter)
		
		return productParameterApi.addParameter(token, storeId, productId, parameter).ifSuccess { saveLocally(it) }
	}
	
	override suspend fun updateParameter(token: String,
	                                     storeId: Long,
	                                     productId: Long,
	                                     parameterId: Long,
	                                     parameter: ProductParameterRequest): ApiResponse<Unit>
	{
		val previousParameter =
				productParameterDao.getById(parameterId).first() ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)
		val updatedParameter =
				previousParameter.copy(name = parameter.name, type = parameter.type, attributes = parameter.attributes)
		
		suspend fun updateLocally() = productParameterDao.update(updatedParameter)
		suspend fun revertLocally() = productParameterDao.update(previousParameter)
		
		updateLocally()
		return productParameterApi.updateParameter(token, storeId, productId, parameterId, parameter)
				.ifFailure { revertLocally() }
	}
	
	override suspend fun removeParameter(token: String, storeId: Long, productId: Long, parameterId: Long): ApiResponse<Unit>
	{
		val removedParameter =
				productParameterDao.getById(parameterId).first() ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)
		
		suspend fun removeLocally() = productParameterDao.delete(removedParameter)
		suspend fun revertLocally() = productParameterDao.insert(removedParameter)
		
		removeLocally()
		return productParameterApi.removeParameter(token, storeId, productId, parameterId).ifFailure { revertLocally() }
	}
	
	override suspend fun cleanLocalParameters() = productParameterDao.deleteAll()
}
