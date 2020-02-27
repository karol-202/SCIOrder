package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.validation.MAX_PASSWORD_LENGTH

object Users : LongIdTable("user")
{
	val password = varchar("password", User.MAX_PASSWORD_LENGTH)
}
