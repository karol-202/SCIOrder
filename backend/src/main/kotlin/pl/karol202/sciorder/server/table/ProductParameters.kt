package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.validation.MAX_NAME_LENGTH
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH

object ProductParameters : LongIdTable("product_parameter")
{
	val productId = reference("product_id", Products,
	                          onUpdate = ReferenceOption.CASCADE,
	                          onDelete = ReferenceOption.CASCADE)
	val name = varchar("name", ProductParameter.MAX_NAME_LENGTH)
	val type = enumeration("type", ProductParameter.Type::class)
	val minimalValue = float("minimal_value").nullable()
	val maximalValue = float("maximal_value").nullable()
	val defaultValue = varchar("default_value", ProductParameter.MAX_VALUE_LENGTH).nullable()
}
