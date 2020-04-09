package pl.karol202.sciorder.client.android.common.database.dao

import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.serialization.builtins.nullable
import pl.karol202.sciorder.client.android.common.database.flowsharedpreferences.FlowSharedPreferencesDao
import pl.karol202.sciorder.client.common.database.dao.UserAuthDao
import pl.karol202.sciorder.common.model.UserLoginResult

class UserAuthDaoImpl(preferences: FlowSharedPreferences) : UserAuthDao
{
	private val delegate = FlowSharedPreferencesDao(preferences, UserLoginResult.serializer().nullable, "dao.userauth")
	
	override suspend fun set(authData: UserLoginResult?) = delegate.set(authData)
	
	override fun get() = delegate.get()
}
