package pl.karol202.sciorder.server.controller.store

import pl.karol202.sciorder.common.request.StoreRequest
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.controller.*
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.store.StoreService

class StoreControllerImpl(private val permissionService: PermissionService,
                          private val storeService: StoreService) : StoreController
{
	override suspend fun postStore(requestHandler: RequestHandler) = requestHandler {
		val store = requireBody<StoreRequest> { isValid }
		requirePrincipal { permissionService.canInsertStore(it) }
		
		val newStore = storeService.insertStore(store)
		created(newStore)
	}
	
	override suspend fun deleteStore(requestHandler: RequestHandler) = requestHandler {
		val storeId = requireLongParameter("storeId")
		requirePrincipal { permissionService.canDeleteStore(it, storeId) }
		
		storeService.deleteStore(storeId)
		ok()
	}
}
