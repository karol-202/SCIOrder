package pl.karol202.sciorder.server.service.admin

import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest
import pl.karol202.sciorder.server.auth.JWTProvider
import pl.karol202.sciorder.server.controller.conflict
import pl.karol202.sciorder.server.controller.forbidden
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.AdminEntity
import pl.karol202.sciorder.server.service.hash.HashService
import pl.karol202.sciorder.server.service.store.StoreService
import pl.karol202.sciorder.server.table.Admins

class AdminServiceImpl(private val hashService: HashService,
                       private val storeService: StoreService,
                       private val jwtProvider: JWTProvider) : AdminService
{
	override suspend fun insertAdmin(admin: AdminRequest): Admin
	{
		if(!AdminEntity.find { Admins.name eq admin.name }.empty()) conflict()
		
		val hash = hashService.hash(admin.password)
		val adminEntity = AdminEntity.new {
			name = admin.name
			password = hash
		}
		return adminEntity.map()
	}
	
	override suspend fun deleteAdmin(adminId: Long)
	{
		val admin = AdminEntity.findById(adminId) ?: notFound()
		admin.delete()
	}
	
	override suspend fun loginAdmin(request: AdminLoginRequest): Pair<Admin, String>
	{
		val hash = hashService.hash(request.password)
		val adminEntity = AdminEntity.find { Admins.name eq request.name }.limit(1).singleOrNull() ?: forbidden()
		if(adminEntity.password != hash) forbidden()
		
		val token = jwtProvider.signForAdmin(adminEntity.id.value)
		return adminEntity.map() to token
	}
}
