package pl.karol202.sciorder.server.service.store

import org.jetbrains.exposed.sql.deleteWhere
import pl.karol202.sciorder.common.model.Store
import pl.karol202.sciorder.common.request.StoreRequest
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.table.Stores

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
		Stores.deleteWhere { Stores.id eq storeId }
	}
}
