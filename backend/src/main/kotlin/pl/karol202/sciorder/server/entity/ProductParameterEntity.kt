package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.entity.mapping.OrderedMappable
import pl.karol202.sciorder.server.entity.mapping.OrderedUpdatable
import pl.karol202.sciorder.server.entity.mapping.dispatchTo
import pl.karol202.sciorder.server.entity.mapping.map
import pl.karol202.sciorder.server.table.ProductParameterEnumValues
import pl.karol202.sciorder.server.table.ProductParameters

class ProductParameterEntity(id: EntityID<Long>) : LongEntity(id),
                                                   OrderedMappable<Product.Parameter>,
                                                   OrderedUpdatable<Product.Parameter>
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
	
	override fun map() = Product.Parameter(id = id.value,
	                                       name = name,
	                                       type = type,
	                                       attributes = Product.Parameter.Attributes(minimalValue = minimalValue,
	                                                                                 maximalValue = maximalValue,
	                                                                                 enumValues = enumValues.map()))
	
	override fun update(model: Product.Parameter)
	{
		name = model.name
		type = model.type
		minimalValue = model.attributes.minimalValue
		maximalValue = model.attributes.maximalValue
		defaultValue = model.attributes.defaultValue
		
		enumValues.dispatchTo(model.attributes.enumValues.orEmpty()) {
			ProductParameterEnumValueEntity.new { productParameter = this@ProductParameterEntity }
		}
	}
}
