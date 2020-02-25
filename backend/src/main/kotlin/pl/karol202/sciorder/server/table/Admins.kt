package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH
import pl.karol202.sciorder.common.validation.PASSWORD_LENGTH

object Admins : LongIdTable()
{
	val name = varchar("name", Admin.MAX_NAME_LENGTH)
	val password = varchar("password", Admin.PASSWORD_LENGTH)
}
