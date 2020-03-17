package pl.karol202.sciorder.client.android.common.database.dao

import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.flow.map
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.Json
import pl.karol202.sciorder.client.common.database.dao.UserDao
import pl.karol202.sciorder.client.common.model.UserWithPassword

private const val KEY_USER = "dao.user"

class UserDaoImpl(private val flowSharedPreferences: FlowSharedPreferences) : UserDao
{
	override suspend fun set(user: UserWithPassword)
	{
		val serialized = Json.stringify(UserWithPassword.serializer(), user)
		flowSharedPreferences.getString(KEY_USER).setAndCommit(serialized)
	}
	
	override fun get() = flowSharedPreferences.getString(KEY_USER).asFlow()
			.map { Json.parse(UserWithPassword.serializer().nullable, it) }
}
