package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.validation.MAX_DETAIL_LENGTH

object OrderTable : IntIdTable()
{
	val storeId = integer("storeId").references(StoreTable.id,
	                                            onUpdate = ReferenceOption.CASCADE,
	                                            onDelete = ReferenceOption.CASCADE)
	val location = varchar("location", Order.Details.MAX_DETAIL_LENGTH)
	val recipient = varchar("recipient", Order.Details.MAX_DETAIL_LENGTH)
	val status = enumeration("status", Order.Status::class)
}
