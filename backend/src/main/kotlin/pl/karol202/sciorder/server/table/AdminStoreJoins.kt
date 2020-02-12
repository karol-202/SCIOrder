package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object AdminStoreJoins : Table()
{
	val adminId = reference("adminId", Admins,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	val storeId = reference("storeId", Stores,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	
	override val primaryKey = PrimaryKey(adminId, storeId)
}
