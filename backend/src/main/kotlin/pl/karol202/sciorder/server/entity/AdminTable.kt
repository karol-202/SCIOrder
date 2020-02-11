package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH

object AdminTable : IntIdTable()
{
	val name = varchar("name", Admin.MAX_NAME_LENGTH)
}
