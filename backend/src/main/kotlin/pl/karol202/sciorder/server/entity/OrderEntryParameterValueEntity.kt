package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.server.entity.mapping.Mappable
import pl.karol202.sciorder.server.table.OrderEntryParameterValues

class OrderEntryParameterValueEntity(id: EntityID<Long>) : LongEntity(id), Mappable<Pair<Long, String>>
{
	companion object : LongEntityClass<OrderEntryParameterValueEntity>(OrderEntryParameterValues)
	
	var orderEntry by OrderEntryEntity referencedOn OrderEntryParameterValues.orderEntryId
	var productParameterEntity by ProductParameterEntity referencedOn OrderEntryParameterValues.productParameterId
	var value by OrderEntryParameterValues.value
	
	override fun map() = productParameterEntity.id.value to value
}
