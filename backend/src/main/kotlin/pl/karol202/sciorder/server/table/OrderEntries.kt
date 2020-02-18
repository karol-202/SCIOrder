package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object OrderEntries : LongIdTable()
{
	val orderId = reference("orderId", Orders,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	val productId = reference("productId", Products,
	                          onUpdate = ReferenceOption.CASCADE,
	                          onDelete = ReferenceOption.CASCADE)
	val quantity = integer("quantity")
}
