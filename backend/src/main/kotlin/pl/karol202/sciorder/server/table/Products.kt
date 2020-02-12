package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH

object Products : LongIdTable()
{
	val storeId = reference("storeId", Stores,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	val name = varchar("name", Product.MAX_NAME_LENGTH)
	val available = bool("available")
}
