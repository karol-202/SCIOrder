package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH
import pl.karol202.sciorder.common.validation.MAX_PASSWORD_LENGTH

object Admins : LongIdTable("admin")
{
	val name = varchar("name", Admin.MAX_NAME_LENGTH)
	val password = varchar("password", Admin.MAX_PASSWORD_LENGTH)
	
	init
	{
		uniqueIndex(name)
	}
}
