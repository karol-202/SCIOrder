package pl.karol202.sciorder.client.android.common.database.flowsharedpreferences

import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class FlowSharedPreferencesDao<T>(private val flowSharedPreferences: FlowSharedPreferences,
                                  private val serializer: KSerializer<T>,
                                  private val keyName: String)
{
	suspend fun set(value: T)
	{
		val serialized = Json.stringify(serializer, value)
		flowSharedPreferences.getString(keyName).setAndCommit(serialized)
	}
	
	fun get() = flowSharedPreferences.getString(keyName)
			.asFlow()
			.map { if(it.isNotBlank()) Json.parse(serializer, it) else null }
}
