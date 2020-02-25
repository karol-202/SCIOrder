package pl.karol202.sciorder.server.service.admin

import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest

interface AdminService
{
	fun insertAdmin(admin: AdminRequest): Admin
	
	fun deleteAdmin(adminId: Long)
	
	fun loginAdmin(request: AdminLoginRequest): String
}
