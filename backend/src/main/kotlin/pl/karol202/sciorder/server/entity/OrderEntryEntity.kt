package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.entity.mapping.Mappable
import pl.karol202.sciorder.server.entity.mapping.map
import pl.karol202.sciorder.server.table.OrderEntries
import pl.karol202.sciorder.server.table.OrderEntryParameterValues
import pl.karol202.sciorder.server.util.SortableEntity

class OrderEntryEntity(id: EntityID<Long>) : LongEntity(id), Mappable<Order.Entry>, SortableEntity
{
	companion object : LongEntityClass<OrderEntryEntity>(OrderEntries)
	
	var order by OrderEntity referencedOn OrderEntries.orderId
	override var ordinal by OrderEntries.ordinal
	var product by ProductEntity referencedOn OrderEntries.productId
	var quantity by OrderEntries.quantity
	
	val parameters by OrderEntryParameterValueEntity referrersOn OrderEntryParameterValues.orderEntryId
	
	override fun map() = Order.Entry(productId = product.id.value,
	                                 quantity = quantity,
	                                 parameters = parameters.map().toMap())
}
