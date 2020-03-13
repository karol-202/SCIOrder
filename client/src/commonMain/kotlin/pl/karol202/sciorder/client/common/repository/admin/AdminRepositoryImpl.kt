package pl.karol202.sciorder.client.common.repository.admin

import pl.karol202.sciorder.client.common.api.admin.AdminApi
import pl.karol202.sciorder.common.request.AdminRequest

class AdminRepositoryImpl(private val adminApi: AdminApi) : AdminRepository
{
	override suspend fun registerAdmin(admin: AdminRequest) = adminApi.registerAdmin(admin)
}
