package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH

object Admins : LongIdTable()
{
	val name = varchar("name", Admin.MAX_NAME_LENGTH)
}