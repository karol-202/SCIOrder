package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.util.Mappable
import pl.karol202.sciorder.server.table.OrderEntryParameterValues

class OrderEntryParameterValueEntity(id: EntityID<Long>) : LongEntity(id), Mappable<Pair<Long, String>>
{
	companion object : LongEntityClass<OrderEntryParameterValueEntity>(OrderEntryParameterValues)
	
	var orderEntry by OrderEntryEntity referencedOn OrderEntryParameterValues.orderEntryId
	var productParameter by ProductParameterEntity referencedOn OrderEntryParameterValues.productParameterId
	var value by OrderEntryParameterValues.value
	
	override fun map() = productParameter.id.value to value
}
