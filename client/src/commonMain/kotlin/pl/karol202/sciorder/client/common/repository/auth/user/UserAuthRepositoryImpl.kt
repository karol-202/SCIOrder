package pl.karol202.sciorder.client.common.repository.auth.user

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.user.UserApi
import pl.karol202.sciorder.client.common.database.dao.UserAuthDao
import pl.karol202.sciorder.common.model.UserLoginResult
import pl.karol202.sciorder.common.request.UserLoginRequest

class UserAuthRepositoryImpl(private val userAuthDao: UserAuthDao,
                             private val userApi: UserApi) : UserAuthRepository
{
	override fun getUserAuthFlow() = userAuthDao.get()
	
	override suspend fun login(request: UserLoginRequest): ApiResponse<UserLoginResult>
	{
		suspend fun saveLocally(result: UserLoginResult) = userAuthDao.set(result)
		
		return userApi.loginUser(request).ifSuccess { saveLocally(it) }
	}
	
	override suspend fun logout() = userAuthDao.set(null)
}
