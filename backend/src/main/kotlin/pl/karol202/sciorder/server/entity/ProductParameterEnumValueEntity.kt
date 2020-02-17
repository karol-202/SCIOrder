package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.server.entity.mapping.OrderedMappable
import pl.karol202.sciorder.server.entity.mapping.OrderedUpdatable
import pl.karol202.sciorder.server.table.ProductParameterEnumValues
import pl.karol202.sciorder.server.util.WithId

class ProductParameterEnumValueEntity(id: EntityID<Long>) : LongEntity(id),
                                                            OrderedMappable<WithId<String>>,
                                                            OrderedUpdatable<WithId<String>>
{
	companion object : LongEntityClass<ProductParameterEnumValueEntity>(ProductParameterEnumValues)
	
	var productParameter by ProductParameterEntity referencedOn ProductParameterEnumValues.productParameterId
	override var ordinal by ProductParameterEnumValues.ordinal
	var value by ProductParameterEnumValues.value
	
	override fun map() = WithId(id.value, value)
	
	override fun update(model: WithId<String>)
	{
		value = model.value
	}
}
