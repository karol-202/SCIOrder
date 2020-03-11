package pl.karol202.sciorder.client.common.api.admin

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.model.AdminLoginResult
import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest

interface AdminApi
{
	suspend fun registerAdmin(admin: AdminRequest): ApiResponse<Admin>
	
	suspend fun loginAdmin(request: AdminLoginRequest): ApiResponse<AdminLoginResult>
}
