package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.table.ProductParameterEnumValues
import pl.karol202.sciorder.server.table.ProductParameters
import pl.karol202.sciorder.server.util.MappableEntity
import pl.karol202.sciorder.server.util.SortableEntity
import pl.karol202.sciorder.server.util.toModels

class ProductParameterEntity(id: EntityID<Long>) : LongEntity(id), MappableEntity<Product.Parameter>, SortableEntity
{
	companion object : LongEntityClass<ProductParameterEntity>(ProductParameters)
	
	var product by ProductEntity referencedOn ProductParameters.productId
	override var ordinal by ProductParameters.ordinal
	var name by ProductParameters.name
	var type by ProductParameters.type
	var minimalValue by ProductParameters.minimalValue
	var maximalValue by ProductParameters.maximalValue
	var defaultValue by ProductParameters.defaultValue
	
	val enumValues by ProductParameterEnumValueEntity referrersOn ProductParameterEnumValues.productParameterId
	
	override fun toModel() = Product.Parameter(id = id.value,
	                                           name = name,
	                                           type = type,
	                                           attributes = Product.Parameter.Attributes(minimalValue = minimalValue,
	                                                                                     maximalValue = maximalValue,
	                                                                                     enumValues = enumValues.toModels()))
}
