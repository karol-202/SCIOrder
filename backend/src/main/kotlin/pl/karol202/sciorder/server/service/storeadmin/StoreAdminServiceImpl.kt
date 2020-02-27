package pl.karol202.sciorder.server.service.storeadmin

import org.jetbrains.exposed.sql.SizedCollection
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.AdminEntity
import pl.karol202.sciorder.server.entity.StoreEntity

class StoreAdminServiceImpl : StoreAdminService
{
	override suspend fun insertStoreAdminJoin(adminId: Long, storeId: Long)
	{
		val adminEntity = AdminEntity.findById(adminId) ?: notFound()
		val storeEntity = StoreEntity.findById(storeId) ?: notFound()
		adminEntity.stores = SizedCollection(adminEntity.stores + storeEntity)
	}
}
