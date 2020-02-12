package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH

// Using single-column primary key instead of composite key from parameterId and enumValueOrdinal
// for consistency with OrderEntryTable and ProductParameterTable
object ProductParameterEnumValues : LongIdTable()
{
	val productParameterId = reference("productParameterId", ProductParameters,
	                                   onUpdate = ReferenceOption.CASCADE,
	                                   onDelete = ReferenceOption.CASCADE)
	val ordinal = integer("ordinal")
	val value = varchar("value", Product.Parameter.MAX_VALUE_LENGTH)
	
	init
	{
		uniqueIndex(productParameterId, ordinal)
	}
}
