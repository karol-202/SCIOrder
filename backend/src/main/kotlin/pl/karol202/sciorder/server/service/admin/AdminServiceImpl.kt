package pl.karol202.sciorder.server.service.admin

import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminRequest
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.AdminEntity
import pl.karol202.sciorder.server.service.store.StoreService

class AdminServiceImpl(private val storeService: StoreService) : AdminService
{
	override fun insertAdmin(admin: AdminRequest): Admin
	{
		val adminEntity = AdminEntity.new {
			name = admin.name
		}
		return adminEntity.map()
	}
	
	override fun deleteAdmin(adminId: Long)
	{
		val admin = AdminEntity.findById(adminId) ?: notFound()
		val storeIds = admin.stores.map { it.id.value }
		
		admin.delete()
		storeIds.forEach { storeService.deleteStoreIfNoAdmins(it) }
	}
}
