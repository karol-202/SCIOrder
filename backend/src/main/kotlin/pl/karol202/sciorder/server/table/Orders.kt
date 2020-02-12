package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.validation.MAX_DETAIL_LENGTH

object Orders : LongIdTable()
{
	val storeId = reference("storeId", Stores,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	val location = varchar("location", Order.Details.MAX_DETAIL_LENGTH)
	val recipient = varchar("recipient", Order.Details.MAX_DETAIL_LENGTH)
	val status = enumeration("status", Order.Status::class)
}
