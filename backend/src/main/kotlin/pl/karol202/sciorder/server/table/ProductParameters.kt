package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH

// Using single-column primary key is necessary (instead of composite key from productId and ordinal)
// because this table is referenced from other tables and currently Exposed (#511) doesn't allow composite foreign keys.
object ProductParameters : LongIdTable()
{
	val productId = reference("productId", Products,
	                          onUpdate = ReferenceOption.CASCADE,
	                          onDelete = ReferenceOption.CASCADE)
	val ordinal = integer("ordinal")
	val name = varchar("name", Product.Parameter.MAX_NAME_LENGTH)
	val type = enumeration("type", Product.Parameter.Type::class)
	val minimalValue = float("minimalValue").nullable()
	val maximalValue = float("maximalValue").nullable()
	val defaultValue = varchar("defaultValue", Product.Parameter.MAX_VALUE_LENGTH).nullable()
	
	init
	{
		uniqueIndex(productId, ordinal)
	}
}
