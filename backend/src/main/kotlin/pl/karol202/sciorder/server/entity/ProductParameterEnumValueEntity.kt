package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.server.table.ProductParameterEnumValues
import pl.karol202.sciorder.server.util.Mappable

class ProductParameterEnumValueEntity(id: EntityID<Long>) : LongEntity(id), Mappable<String>
{
	companion object : LongEntityClass<ProductParameterEnumValueEntity>(ProductParameterEnumValues)
	
	var productParameter by ProductParameterEntity referencedOn ProductParameterEnumValues.productParameterId
	var value by ProductParameterEnumValues.value
	
	override fun map() = value
}
