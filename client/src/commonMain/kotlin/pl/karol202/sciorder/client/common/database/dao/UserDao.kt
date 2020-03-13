package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.model.UserWithPassword

// UserDao stores single user that is created at first launch of the application
// and remains unchanged across all the logins and logouts from stores
interface UserDao
{
	suspend fun set(user: UserWithPassword)
	
	fun get(): Flow<UserWithPassword?>
}
