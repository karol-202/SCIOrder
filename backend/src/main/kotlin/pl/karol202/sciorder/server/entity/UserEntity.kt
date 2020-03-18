package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.server.table.Orders
import pl.karol202.sciorder.server.table.Users
import pl.karol202.sciorder.server.util.Mappable

class UserEntity(id: EntityID<Long>) : LongEntity(id), Mappable<User>
{
	companion object : LongEntityClass<UserEntity>(Users)
	
	var password by Users.password
	
	val orders by OrderEntity optionalReferrersOn Orders.userId
	
	override fun map() = User(id = id.value)
}
