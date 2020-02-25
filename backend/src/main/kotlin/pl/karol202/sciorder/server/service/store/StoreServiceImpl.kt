package pl.karol202.sciorder.server.service.store

import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.StoreEntity

class StoreServiceImpl : StoreService
{
	override fun insertStore(store: StoreRequest): Store
	{
		val storeEntity = StoreEntity.new {
			name = store.name
		}
		return storeEntity.map()
	}
	
	override fun deleteStore(storeId: Long)
	{
		val store = StoreEntity.findById(storeId) ?: notFound()
		store.delete()
	}
	
	override fun deleteStoreIfNoAdmins(storeId: Long)
	{
		val store = StoreEntity.findById(storeId) ?: notFound()
		if(store.admins.empty()) store.delete()
	}
}
