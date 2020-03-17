package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.util.Mappable
import pl.karol202.sciorder.common.util.map
import pl.karol202.sciorder.server.table.OrderEntries
import pl.karol202.sciorder.server.table.Orders

class OrderEntity(id: EntityID<Long>) : LongEntity(id), Mappable<Order>
{
	companion object : LongEntityClass<OrderEntity>(Orders)
	
	var store by StoreEntity referencedOn Orders.storeId
	var user by UserEntity optionalReferencedOn Orders.userId
	var location by Orders.location
	var recipient by Orders.recipient
	var status by Orders.status
	
	val entries by OrderEntryEntity referrersOn OrderEntries.orderId
	
	override fun map() = Order(id = id.value,
	                           storeId = store.id.value,
	                           entries = entries.map(),
	                           details = Order.Details(location = location,
	                                                   recipient = recipient),
	                           status = status)
}
