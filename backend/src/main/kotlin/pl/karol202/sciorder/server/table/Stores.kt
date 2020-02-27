package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH

object Stores : LongIdTable("store")
{
	val name = varchar("name", Store.MAX_NAME_LENGTH)
	
	init
	{
		uniqueIndex(name)
	}
}
