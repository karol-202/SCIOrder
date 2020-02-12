package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.server.table.AdminStoreJoins
import pl.karol202.sciorder.server.table.Admins
import pl.karol202.sciorder.server.util.MappableEntity

class AdminEntity(id: EntityID<Long>) : LongEntity(id), MappableEntity<Admin>
{
	companion object : LongEntityClass<AdminEntity>(Admins)
	
	var name by Admins.name
	
	var stores by StoreEntity via AdminStoreJoins
	
	override fun toModel() = Admin(id = id.value,
	                               name = name)
}
