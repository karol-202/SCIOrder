package pl.karol202.sciorder.server.entity

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object AdminStoreJoinTable : Table()
{
	val adminId = integer("adminId").references(AdminTable.id,
	                                            onUpdate = ReferenceOption.CASCADE,
	                                            onDelete = ReferenceOption.CASCADE)
	val storeId = integer("storeId").references(StoreTable.id,
	                                            onUpdate = ReferenceOption.CASCADE,
	                                            onDelete = ReferenceOption.CASCADE)
	
	override val primaryKey = PrimaryKey(adminId, storeId)
}
