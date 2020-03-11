package pl.karol202.sciorder.client.common.api.user

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.model.UserLoginResult
import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest

interface UserApi
{
	suspend fun registerUser(user: UserRequest): ApiResponse<User>
	
	suspend fun loginUser(request: UserLoginRequest): ApiResponse<UserLoginResult>
}
