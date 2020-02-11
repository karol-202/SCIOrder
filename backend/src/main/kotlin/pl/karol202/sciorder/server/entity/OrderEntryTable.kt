package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

// Using single-column primary key is necessary (instead of composite key from orderId and ordinal)
// because this table is referenced from other tables and currently Exposed (#511) doesn't allow composite foreign keys.
object OrderEntryTable : IntIdTable()
{
	val orderId = integer("orderId").references(OrderTable.id,
	                                            onUpdate = ReferenceOption.CASCADE,
	                                            onDelete = ReferenceOption.CASCADE)
	val ordinal = integer("ordinal")
	val productId = integer("productId").references(ProductTable.id,
	                                                onUpdate = ReferenceOption.CASCADE,
	                                                onDelete = ReferenceOption.CASCADE)
	val quantity = integer("quantity")
	
	init
	{
		uniqueIndex(orderId, ordinal)
	}
}
