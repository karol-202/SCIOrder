package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

// Using single-column primary key is necessary (instead of composite key from orderId and ordinal)
// because this table is referenced from other tables and currently Exposed (#511) doesn't allow composite foreign keys.
object OrderEntries : LongIdTable()
{
	val orderId = reference("orderId", Orders,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	val ordinal = integer("ordinal")
	val productId = reference("productId", Products,
	                          onUpdate = ReferenceOption.CASCADE,
	                          onDelete = ReferenceOption.CASCADE)
	val quantity = integer("quantity")
	
	init
	{
		uniqueIndex(orderId, ordinal)
	}
}
