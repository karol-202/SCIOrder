package pl.karol202.sciorder.server.service.store

import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest

interface StoreService
{
	fun insertStore(store: StoreRequest): Store
	
	fun deleteStore(storeId: Long)
	
	fun deleteStoreIfNoAdmins(storeId: Long)
}
