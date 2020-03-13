package pl.karol202.sciorder.client.common.repository.admin

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.request.AdminRequest

interface AdminRepository
{
	suspend fun registerAdmin(admin: AdminRequest): ApiResponse<Admin>
}
