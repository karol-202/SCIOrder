package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.UserLoginResult

interface UserAuthDao
{
	suspend fun set(authData: UserLoginResult)
	
	fun get(): Flow<UserLoginResult?>
}
