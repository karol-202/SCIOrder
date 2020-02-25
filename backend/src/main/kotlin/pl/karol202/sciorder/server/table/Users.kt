package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.validation.PASSWORD_LENGTH

object Users : LongIdTable()
{
	val password = varchar("password", User.PASSWORD_LENGTH)
}
