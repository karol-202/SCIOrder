package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.MAX_VALUE_LENGTH

// Using single-column primary key instead of composite key from parameterId and enumValueOrdinal
// for consistency with OrderEntryTable and ProductParameterTable
object OrderParameterValueTable : IntIdTable()
{
	val orderEntryId = integer("orderEntryId").references(OrderEntryTable.id,
	                                                      onUpdate = ReferenceOption.CASCADE,
	                                                      onDelete = ReferenceOption.CASCADE)
	val productParameterId = integer("productParameterId").references(ProductParameterTable.id,
	                                                                  onUpdate = ReferenceOption.CASCADE,
	                                                                  onDelete = ReferenceOption.CASCADE)
	val value = varchar("value", Product.Parameter.MAX_VALUE_LENGTH)
	
	init
	{
		uniqueIndex(orderEntryId, productParameterId)
	}
}
