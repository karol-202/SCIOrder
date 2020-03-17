package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.Mappable
import pl.karol202.sciorder.common.util.map
import pl.karol202.sciorder.server.table.ProductParameterEnumValues
import pl.karol202.sciorder.server.table.ProductParameters

class ProductParameterEntity(id: EntityID<Long>) : LongEntity(id), Mappable<ProductParameter>
{
	companion object : LongEntityClass<ProductParameterEntity>(ProductParameters)
	
	var product by ProductEntity referencedOn ProductParameters.productId
	var name by ProductParameters.name
	var type by ProductParameters.type
	var minimalValue by ProductParameters.minimalValue
	var maximalValue by ProductParameters.maximalValue
	var defaultValue by ProductParameters.defaultValue
	
	val enumValues by ProductParameterEnumValueEntity referrersOn ProductParameterEnumValues.productParameterId
	
	override fun map() = ProductParameter(id = id.value,
	                                      productId = product.id.value,
	                                      name = name,
	                                      type = type,
	                                      attributes = ProductParameter.Attributes(minimalValue = minimalValue,
	                                                                               maximalValue = maximalValue,
	                                                                               enumValues = enumValues.map(),
	                                                                               defaultValue = defaultValue))
}
