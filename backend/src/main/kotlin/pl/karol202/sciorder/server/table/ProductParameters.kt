package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH

object ProductParameters : LongIdTable()
{
	val productId = reference("productId", Products,
	                          onUpdate = ReferenceOption.CASCADE,
	                          onDelete = ReferenceOption.CASCADE)
	val name = varchar("name", ProductParameter.MAX_NAME_LENGTH)
	val type = enumeration("type", ProductParameter.Type::class)
	val minimalValue = float("minimalValue").nullable()
	val maximalValue = float("maximalValue").nullable()
	val defaultValue = varchar("defaultValue", ProductParameter.MAX_VALUE_LENGTH).nullable()
}
