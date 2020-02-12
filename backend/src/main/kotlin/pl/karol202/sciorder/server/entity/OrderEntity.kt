package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.table.OrderEntries
import pl.karol202.sciorder.server.table.Orders
import pl.karol202.sciorder.server.util.MappableEntity
import pl.karol202.sciorder.server.util.toModels

class OrderEntity(id: EntityID<Long>) : LongEntity(id), MappableEntity<Order>
{
	companion object : LongEntityClass<OrderEntity>(Orders)
	
	var store by StoreEntity referencedOn Orders.storeId
	var location by Orders.location
	var recipient by Orders.recipient
	var status by Orders.status
	
	val entries by OrderEntryEntity referrersOn OrderEntries.orderId
	
	override fun toModel() = Order(id = id.value,
	                               storeId = store.id.value,
	                               entries = entries.toModels(),
	                               details = Order.Details(location = location,
	                                                       recipient = recipient),
	                               status = status)
}