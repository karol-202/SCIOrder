package pl.karol202.sciorder.client.common.repository.user

import kotlinx.coroutines.flow.distinctUntilChanged
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.user.UserApi
import pl.karol202.sciorder.client.common.database.dao.UserDao
import pl.karol202.sciorder.client.common.model.UserWithPassword
import pl.karol202.sciorder.common.request.UserRequest

class UserRepositoryImpl(private val userDao: UserDao,
                         private val userApi: UserApi) : UserRepository
{
	override fun getUserFlow() = userDao.get().distinctUntilChanged()
	
	override suspend fun registerUser(user: UserRequest): ApiResponse<UserWithPassword>
	{
		suspend fun saveLocally(user: UserWithPassword) = userDao.set(user)
		
		return userApi.registerUser(user).map { UserWithPassword(it, user.password) }.ifSuccess { saveLocally(it) }
	}
}
