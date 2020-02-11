package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH

object ProductTable : IntIdTable()
{
	val storeId = integer("storeId").references(StoreTable.id,
	                                            onUpdate = ReferenceOption.CASCADE,
	                                            onDelete = ReferenceOption.CASCADE)
	val name = varchar("name", Product.MAX_NAME_LENGTH)
	val available = bool("available")
}
