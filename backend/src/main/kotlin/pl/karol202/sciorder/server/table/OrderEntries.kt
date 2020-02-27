package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object OrderEntries : LongIdTable("order_entry")
{
	val orderId = reference("order_id", Orders,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	val productId = reference("product_id", Products,
	                          onUpdate = ReferenceOption.CASCADE,
	                          onDelete = ReferenceOption.CASCADE)
	val quantity = integer("quantity")
}
