package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.api.ApiResponse

class StandardMixedResource<T>(private val updateIntervalMillis: Long,
                               private val getFromApi: suspend () -> ApiResponse<List<T>>,
                               getFromDB: () -> Flow<List<T>>,
                               private val saveToDB: suspend (List<T>) -> Unit) : AbstractMixedResource<List<T>>(getFromDB())
{
	override suspend fun waitForNextUpdate() = delay(updateIntervalMillis)
	
	override suspend fun loadFromApi() = getFromApi()
	
	override suspend fun saveToDatabase(data: List<T>) = saveToDB(data)
}
