package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.server.table.AdminStoreJoins
import pl.karol202.sciorder.server.table.Orders
import pl.karol202.sciorder.server.table.Products
import pl.karol202.sciorder.server.table.Stores
import pl.karol202.sciorder.server.util.MappableEntity

class StoreEntity(id: EntityID<Long>) : LongEntity(id), MappableEntity<Store>
{
	companion object : LongEntityClass<StoreEntity>(Stores)
	
	var name by Stores.name
	
	var admins by AdminEntity via AdminStoreJoins
	val orders by OrderEntity referrersOn Orders.storeId
	val products by ProductEntity referrersOn Products.storeId
	
	override fun toModel() = Store(id = id.value,
	                               name = name)
}
