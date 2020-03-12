package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.database.dao.CrudDao
import pl.karol202.sciorder.client.common.database.dao.dispatchDiff
import pl.karol202.sciorder.common.util.IdProvider

class StandardMixedResource<T : IdProvider, D : CrudDao<T>>(private val dao: D,
                                                            databaseProvider: D.() -> Flow<List<T>>,
                                                            private val apiProvider: suspend () -> ApiResponse<List<T>>,
                                                            private val updateIntervalMillis: Long) :
		AbstractMixedResource<List<T>>(dao.databaseProvider())
{
	override suspend fun waitForNextUpdate() = delay(updateIntervalMillis)
	
	override suspend fun loadFromApi() = apiProvider()
	
	override suspend fun saveToDatabase(data: List<T>) = dao.dispatchDiff(databaseFlow.first(), data)
}
