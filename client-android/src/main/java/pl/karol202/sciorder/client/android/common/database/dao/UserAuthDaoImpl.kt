package pl.karol202.sciorder.client.android.common.database.dao

import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.flow.map
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.Json
import pl.karol202.sciorder.client.common.database.dao.UserAuthDao
import pl.karol202.sciorder.common.model.UserLoginResult

private const val KEY_USER_AUTH = "dao.userauth"

class UserAuthDaoImpl(private val flowSharedPreferences: FlowSharedPreferences) : UserAuthDao
{
	override suspend fun set(loginResult: UserLoginResult?)
	{
		val serialized = Json.stringify(UserLoginResult.serializer().nullable, loginResult)
		flowSharedPreferences.getString(KEY_USER_AUTH).setAndCommit(serialized)
	}
	
	override fun get() = flowSharedPreferences.getString(KEY_USER_AUTH).asFlow()
			.map { Json.parse(UserLoginResult.serializer().nullable, it) }
}
