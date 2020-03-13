package pl.karol202.sciorder.client.common.repository.auth.user

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.UserLoginResult
import pl.karol202.sciorder.common.request.UserLoginRequest

interface UserAuthRepository
{
	fun getUserAuthFlow(): Flow<UserLoginResult?>
	
	suspend fun loginUser(request: UserLoginRequest): ApiResponse<UserLoginResult>
}
