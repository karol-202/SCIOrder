package pl.karol202.sciorder.client.android.common.database.dao

import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.serialization.builtins.nullable
import pl.karol202.sciorder.client.android.common.database.flowsharedpreferences.FlowSharedPreferencesDao
import pl.karol202.sciorder.client.common.database.dao.AdminAuthDao
import pl.karol202.sciorder.common.model.AdminLoginResult

class AdminAuthDaoImpl(preferences: FlowSharedPreferences) : AdminAuthDao
{
	private val delegate = FlowSharedPreferencesDao(preferences, AdminLoginResult.serializer().nullable, "dao.adminauth")
	
	override suspend fun set(authData: AdminLoginResult?) = delegate.set(authData)
	
	override fun get() = delegate.get()
}
