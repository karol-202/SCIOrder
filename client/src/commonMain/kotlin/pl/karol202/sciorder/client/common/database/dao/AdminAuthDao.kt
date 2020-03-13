package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.AdminLoginResult

interface AdminAuthDao
{
	suspend fun set(authData: AdminLoginResult?)
	
	fun get(): Flow<AdminLoginResult?>
}
