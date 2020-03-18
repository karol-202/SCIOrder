package pl.karol202.sciorder.server.service.store

import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest
import pl.karol202.sciorder.server.controller.conflict
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.AdminEntity
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.table.Stores
import pl.karol202.sciorder.server.util.map

class StoreServiceImpl : StoreService
{
	override suspend fun insertStore(store: StoreRequest): Store
	{
		if(!StoreEntity.find { Stores.name eq store.name }.empty()) conflict()
		val storeEntity = StoreEntity.new {
			name = store.name
		}
		return storeEntity.map()
	}
	
	override suspend fun deleteStore(storeId: Long)
	{
		val storeEntity = StoreEntity.findById(storeId) ?: notFound()
		storeEntity.delete()
	}
	
	override suspend fun getStoresByAdmin(adminId: Long): List<Store>
	{
		val adminEntity = AdminEntity.findById(adminId) ?: notFound()
		return adminEntity.stores.map()
	}
}
