package pl.karol202.sciorder.server.service.admin

import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest

interface AdminService
{
	suspend fun insertAdmin(admin: AdminRequest): Admin
	
	suspend fun deleteAdmin(adminId: Long)
	
	suspend fun loginAdmin(request: AdminLoginRequest): Pair<Admin, String>
}
