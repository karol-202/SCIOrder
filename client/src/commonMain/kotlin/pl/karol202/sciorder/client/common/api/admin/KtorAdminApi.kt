package pl.karol202.sciorder.client.common.api.admin

import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.Admin
import pl.karol202.sciorder.common.model.AdminLoginResult
import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest

class KtorAdminApi(private val basicApi: KtorBasicApi) : AdminApi
{
	override suspend fun registerAdmin(admin: AdminRequest) = basicApi.post<Admin> {
		relativePath("api/admins/register")
		jsonBody(admin)
	}
	
	override suspend fun loginAdmin(request: AdminLoginRequest) = basicApi.post<AdminLoginResult> {
		relativePath("api/admins/login")
		jsonBody(request)
	}
}
