package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH

object ProductParameterEnumValues : LongIdTable()
{
	val productParameterId = reference("productParameterId", ProductParameters,
	                                   onUpdate = ReferenceOption.CASCADE,
	                                   onDelete = ReferenceOption.CASCADE)
	val value = varchar("value", ProductParameter.MAX_VALUE_LENGTH)
}
