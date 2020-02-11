package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH

object StoreTable : IntIdTable()
{
	val name = varchar("name", Store.MAX_NAME_LENGTH)
}
