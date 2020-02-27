package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH

object OrderEntryParameterValues : LongIdTable("order_entry_parameter_value")
{
	val orderEntryId = reference("order_entry_id", OrderEntries,
	                             onUpdate = ReferenceOption.CASCADE,
	                             onDelete = ReferenceOption.CASCADE)
	val productParameterId = reference("product_parameter_id", ProductParameters,
	                                   onUpdate = ReferenceOption.CASCADE,
	                                   onDelete = ReferenceOption.CASCADE)
	val value = varchar("value", ProductParameter.MAX_VALUE_LENGTH)
	
	init
	{
		uniqueIndex(orderEntryId, productParameterId)
	}
}
