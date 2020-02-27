package pl.karol202.sciorder.server.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object AdminStoreJoins : Table("admin_store_join")
{
	val adminId = reference("admin_id", Admins,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	val storeId = reference("store_id", Stores,
	                        onUpdate = ReferenceOption.CASCADE,
	                        onDelete = ReferenceOption.CASCADE)
	
	override val primaryKey = PrimaryKey(adminId, storeId)
}
