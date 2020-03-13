package pl.karol202.sciorder.client.common.repository.auth.admin

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.admin.AdminApi
import pl.karol202.sciorder.client.common.database.dao.AdminAuthDao
import pl.karol202.sciorder.common.model.AdminLoginResult
import pl.karol202.sciorder.common.request.AdminLoginRequest

class AdminAuthRepositoryImpl(private val adminAuthDao: AdminAuthDao,
                              private val adminApi: AdminApi) : AdminAuthRepository
{
	override fun getAdminAuthFlow() = adminAuthDao.get()
	
	override suspend fun login(request: AdminLoginRequest): ApiResponse<AdminLoginResult>
	{
		suspend fun saveLocally(result: AdminLoginResult) = adminAuthDao.set(result)
		
		return adminApi.loginAdmin(request).ifSuccess { saveLocally(it) }
	}
	
	override suspend fun logout() = adminAuthDao.set(null)
}
