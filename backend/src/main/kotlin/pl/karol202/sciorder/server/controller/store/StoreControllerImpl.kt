package pl.karol202.sciorder.server.controller.store

import pl.karol202.sciorder.common.request.StoreRequest
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.controller.*
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.store.StoreService
import pl.karol202.sciorder.server.service.storeadmin.StoreAdminService

class StoreControllerImpl(private val permissionService: PermissionService,
                          private val storeService: StoreService,
                          private val storeAdminService: StoreAdminService) : StoreController
{
	override suspend fun postStore(requestHandler: RequestHandler) = requestHandler {
		val store = requireBody<StoreRequest> { isValid }
		val principal = requirePrincipal { permissionService.canInsertStore(it) }
		val adminId = (principal as? AbstractPrincipal.AdminPrincipal)?.adminId ?: forbidden()
		
		val newStore = storeService.insertStore(store)
		storeAdminService.insertStoreAdminJoin(adminId, newStore.id)
		created(newStore)
	}
	
	override suspend fun deleteStore(requestHandler: RequestHandler) = requestHandler {
		val storeId = requireLongParameter("storeId")
		requirePrincipal { permissionService.canDeleteStore(it, storeId) }
		
		storeService.deleteStore(storeId)
		ok()
	}
}
