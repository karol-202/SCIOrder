package pl.karol202.sciorder.client.common.repository.user

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.request.UserRequest

interface UserRepository
{
	fun getUserFlow(): Flow<User?>
	
	suspend fun registerUser(user: UserRequest): ApiResponse<User>
}
