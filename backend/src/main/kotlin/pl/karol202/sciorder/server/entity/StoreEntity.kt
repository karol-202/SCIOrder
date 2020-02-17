package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.server.entity.mapping.Mappable
import pl.karol202.sciorder.server.table.AdminStoreJoins
import pl.karol202.sciorder.server.table.Orders
import pl.karol202.sciorder.server.table.Products
import pl.karol202.sciorder.server.table.Stores

class StoreEntity(id: EntityID<Long>) : LongEntity(id), Mappable<Store>
{
	companion object : LongEntityClass<StoreEntity>(Stores)
	
	var name by Stores.name
	
	var admins by AdminEntity via AdminStoreJoins
	val orders by OrderEntity referrersOn Orders.storeId
	val products by ProductEntity referrersOn Products.storeId
	
	override fun map() = Store(id = id.value,
	                           name = name)
}
