package pl.karol202.sciorder.server.controller.admin

import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.controller.*
import pl.karol202.sciorder.server.service.admin.AdminService
import pl.karol202.sciorder.server.service.permission.PermissionService

class AdminControllerImpl(private val permissionService: PermissionService,
                          private val adminService: AdminService) : AdminController
{
	override suspend fun postAdmin(requestHandler: RequestHandler) = requestHandler {
		val admin = requireBody<AdminRequest> { isValid }
		
		val newAdmin = adminService.insertAdmin(admin)
		created(newAdmin)
	}
	
	override suspend fun deleteAdmin(requestHandler: RequestHandler) = requestHandler {
		val adminId = requireLongParameter("adminId")
		requirePrincipal { permissionService.canDeleteAdmin(it, adminId) }
		
		adminService.deleteAdmin(adminId)
		ok()
	}
	
	override suspend fun loginAdmin(requestHandler: RequestHandler) = requestHandler {
		val loginRequest = requireBody<AdminLoginRequest>()
		
		val result = adminService.loginAdmin(loginRequest)
		ok(result)
	}
}
