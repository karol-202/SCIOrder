package pl.karol202.sciorder.server.service.admin

import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest
import pl.karol202.sciorder.server.auth.JWTProvider
import pl.karol202.sciorder.server.controller.forbidden
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.AdminEntity
import pl.karol202.sciorder.server.service.store.StoreService
import pl.karol202.sciorder.server.table.Admins

class AdminServiceImpl(private val storeService: StoreService,
                       private val jwtProvider: JWTProvider) : AdminService
{
	override suspend fun insertAdmin(admin: AdminRequest): Admin
	{
		val adminEntity = AdminEntity.new {
			name = admin.name
			password = admin.password
		}
		return adminEntity.map()
	}
	
	override suspend fun deleteAdmin(adminId: Long)
	{
		val admin = AdminEntity.findById(adminId) ?: notFound()
		val storeIds = admin.stores.map { it.id.value }
		
		admin.delete()
		storeIds.forEach { storeService.deleteStoreIfNoAdmins(it) }
	}
	
	override suspend fun loginAdmin(request: AdminLoginRequest): String
	{
		val adminEntity = AdminEntity.find { Admins.name eq request.name }.limit(1).singleOrNull() ?: forbidden()
		if(adminEntity.password != request.password) forbidden()
		return jwtProvider.signForAdmin(adminEntity.id.value)
	}
}
