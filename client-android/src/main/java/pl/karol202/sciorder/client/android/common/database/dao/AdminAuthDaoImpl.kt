package pl.karol202.sciorder.client.android.common.database.dao

import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.flow.map
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.Json
import pl.karol202.sciorder.client.common.database.dao.AdminAuthDao
import pl.karol202.sciorder.common.model.AdminLoginResult

private const val KEY_ADMIN_AUTH = "dao.adminauth"

class AdminAuthDaoImpl(private val flowSharedPreferences: FlowSharedPreferences) : AdminAuthDao
{
	override suspend fun set(loginResult: AdminLoginResult?)
	{
		val serialized = Json.stringify(AdminLoginResult.serializer().nullable, loginResult)
		flowSharedPreferences.getString(KEY_ADMIN_AUTH).setAndCommit(serialized)
	}
	
	override fun get() = flowSharedPreferences.getString(KEY_ADMIN_AUTH).asFlow()
			.map { Json.parse(AdminLoginResult.serializer().nullable, it) }
}
