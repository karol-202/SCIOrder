package pl.karol202.sciorder.client.common.api.user

import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.jsonBody
import pl.karol202.sciorder.client.common.api.relativePath
import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.model.UserLoginResult
import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest

class KtorUserApi(private val basicApi: KtorBasicApi) : UserApi
{
	override suspend fun registerUser(user: UserRequest) = basicApi.post<User> {
		relativePath("api/users/register")
		jsonBody(user)
	}
	
	override suspend fun loginUser(request: UserLoginRequest) = basicApi.post<UserLoginResult> {
		relativePath("api/users/login")
		jsonBody(request)
	}
}
