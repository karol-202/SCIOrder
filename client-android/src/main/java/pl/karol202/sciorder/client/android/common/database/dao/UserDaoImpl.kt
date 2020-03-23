package pl.karol202.sciorder.client.android.common.database.dao

import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.serialization.internal.nullable
import pl.karol202.sciorder.client.android.common.database.flowsharedpreferences.FlowSharedPreferencesDao
import pl.karol202.sciorder.client.common.database.dao.UserDao
import pl.karol202.sciorder.client.common.model.UserWithPassword

class UserDaoImpl(preferences: FlowSharedPreferences) : UserDao
{
	private val delegate = FlowSharedPreferencesDao(preferences, UserWithPassword.serializer().nullable, "dao.user")
	
	override suspend fun set(user: UserWithPassword) = delegate.set(user)
	
	override fun get() = delegate.get()
}
