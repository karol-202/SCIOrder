package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.OrderEntry
import pl.karol202.sciorder.server.table.OrderEntries
import pl.karol202.sciorder.server.table.OrderEntryParameterValues
import pl.karol202.sciorder.server.util.Mappable
import pl.karol202.sciorder.server.util.map

class OrderEntryEntity(id: EntityID<Long>) : LongEntity(id), Mappable<OrderEntry>
{
	companion object : LongEntityClass<OrderEntryEntity>(OrderEntries)
	
	var order by OrderEntity referencedOn OrderEntries.orderId
	var product by ProductEntity referencedOn OrderEntries.productId
	var quantity by OrderEntries.quantity
	
	val parameters by OrderEntryParameterValueEntity referrersOn OrderEntryParameterValues.orderEntryId
	
	override fun map() = OrderEntry(id = id.value,
	                                productId = product.id.value,
	                                quantity = quantity,
	                                parameters = parameters.map().toMap())
}
