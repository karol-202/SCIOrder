package pl.karol202.sciorder.client.common.repository.user

import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.user.UserApi
import pl.karol202.sciorder.client.common.database.dao.UserDao
import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.request.UserRequest

class UserRepositoryImpl(private val userDao: UserDao,
                         private val userApi: UserApi) : UserRepository
{
	override fun getUserFlow() = userDao.get()
	
	override suspend fun registerUser(user: UserRequest): ApiResponse<User>
	{
		suspend fun saveLocally(user: User) = userDao.set(user)
		
		return userApi.registerUser(user).ifSuccess { saveLocally(it) }
	}
}
