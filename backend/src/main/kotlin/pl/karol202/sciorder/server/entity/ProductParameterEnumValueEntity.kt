package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.server.table.ProductParameterEnumValues
import pl.karol202.sciorder.server.util.MappableEntity
import pl.karol202.sciorder.server.util.SortableEntity

class ProductParameterEnumValueEntity(id: EntityID<Long>) : LongEntity(id), MappableEntity<String>, SortableEntity
{
	companion object : LongEntityClass<ProductParameterEnumValueEntity>(ProductParameterEnumValues)
	
	var productParameter by ProductParameterEntity referencedOn ProductParameterEnumValues.productParameterId
	override var ordinal by ProductParameterEnumValues.ordinal
	var value by ProductParameterEnumValues.value
	
	override fun toModel() = value
}
