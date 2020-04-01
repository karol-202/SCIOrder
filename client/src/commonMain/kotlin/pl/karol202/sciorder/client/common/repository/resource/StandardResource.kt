package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.api.ApiResponse

class StandardResource<T>(private val updateIntervalMillis: Long,
                          private val getFromApi: suspend () -> ApiResponse<T>,
                          getFromDB: () -> Flow<T>,
                          private val saveToDB: suspend (T) -> Unit) : AbstractResource<T>(getFromDB())
{
	override suspend fun waitForNextUpdate() = delay(updateIntervalMillis)
	
	override suspend fun loadFromApi() = getFromApi()
	
	override suspend fun saveToDatabase(data: T) = saveToDB(data)
}
