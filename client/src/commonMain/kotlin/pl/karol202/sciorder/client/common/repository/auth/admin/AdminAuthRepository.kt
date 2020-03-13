package pl.karol202.sciorder.client.common.repository.auth.admin

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.AdminLoginResult
import pl.karol202.sciorder.common.request.AdminLoginRequest

interface AdminAuthRepository
{
	fun getAdminAuthFlow(): Flow<AdminLoginResult?>
	
	suspend fun login(request: AdminLoginRequest): ApiResponse<AdminLoginResult>
}
