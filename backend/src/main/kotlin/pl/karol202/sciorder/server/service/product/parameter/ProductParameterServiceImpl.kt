package pl.karol202.sciorder.server.service.product.parameter

import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.request.ProductParameterRequest
import pl.karol202.sciorder.common.util.map
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.ProductEntity
import pl.karol202.sciorder.server.entity.ProductParameterEntity
import pl.karol202.sciorder.server.entity.ProductParameterEnumValueEntity
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.table.ProductParameters

class ProductParameterServiceImpl : ProductParameterService
{
	override suspend fun insertParameter(storeId: Long, productId: Long, parameter: ProductParameterRequest): ProductParameter
	{
		val productEntity = ProductEntity.findById(productId)?.takeIf { it.store.id.value == storeId } ?: notFound()
		val parameterEntity = ProductParameterEntity.new {
			product = productEntity
			name = parameter.name
			type = parameter.type
			minimalValue = parameter.attributes.minimalValue
			maximalValue = parameter.attributes.maximalValue
			defaultValue = parameter.attributes.defaultValue
		}
		parameter.attributes.enumValues?.forEach { insertEnumValue(parameterEntity, it) }
		return parameterEntity.map()
	}
	
	override suspend fun updateParameter(storeId: Long, productId: Long, parameterId: Long, parameter: ProductParameterRequest)
	{
		val parameterEntity = ProductParameterEntity.findById(parameterId)?.takeIf {
			it.product.store.id.value == storeId && it.product.id.value == productId
		} ?: notFound()
		parameterEntity.name = parameter.name
		parameterEntity.type = parameter.type
		parameterEntity.minimalValue = parameter.attributes.minimalValue
		parameterEntity.maximalValue = parameter.attributes.maximalValue
		parameterEntity.defaultValue = parameter.attributes.defaultValue
		parameterEntity.enumValues.forEach { it.delete() }
		parameter.attributes.enumValues?.forEach { insertEnumValue(parameterEntity, it) }
	}
	
	override suspend fun deleteParameter(storeId: Long, productId: Long, parameterId: Long)
	{
		val parameterEntity = ProductParameterEntity.findById(parameterId)
				?.takeIf(storeId = storeId, productId = productId) ?: notFound()
		parameterEntity.delete()
	}
	
	override suspend fun getParameters(storeId: Long, productId: Long): List<ProductParameter>
	{
		StoreEntity.findById(storeId) ?: notFound()
		ProductEntity.findById(productId)?.takeIf { it.store.id.value == storeId } ?: notFound()
		return ProductParameterEntity.find { ProductParameters.productId eq productId }.map()
	}
	
	private fun insertEnumValue(parameterEntity: ProductParameterEntity, enumValue: String) =
			ProductParameterEnumValueEntity.new {
				productParameter = parameterEntity
				value = enumValue
			}
	
	private fun ProductParameterEntity.takeIf(storeId: Long, productId: Long) =
			takeIf { it.product.store.id.value == storeId && it.product.id.value == productId }
}
