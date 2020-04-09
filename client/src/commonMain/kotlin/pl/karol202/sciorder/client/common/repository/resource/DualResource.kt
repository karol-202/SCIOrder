package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.merge

class DualResource<T1, T2, T>(updateIntervalMillis: Long,
                              private val source1: Source<T1>,
                              private val source2: Source<T2>,
                              private val mix: (List<T1>, List<T2>) -> List<T>) : Resource<List<T>>
{
	class Source<T>(val getFromApi: suspend () -> ApiResponse<List<T>>,
	                val getFromDB: () -> Flow<List<T>>,
	                val saveToDB: suspend (List<T>) -> Unit)
	
	private val delegate = StandardResource(updateIntervalMillis = updateIntervalMillis,
	                                        getFromApi = this::loadFromApi,
	                                        getFromDB = this::getFromDB,
	                                        saveToDB = this::saveToDatabase)
	
	override val asFlow = delegate.asFlow.map { it.map { (first, second) -> mix(first, second) } }
	
	override suspend fun autoReloadIn(coroutineScope: CoroutineScope) = delegate.autoReloadIn(coroutineScope)
	
	override suspend fun reload() = delegate.reload()
	
	override suspend fun close() = delegate.close()
	
	private suspend fun loadFromApi() = coroutineScope {
		val async1 = async { source1.getFromApi() }
		val async2 = async { source2.getFromApi() }
		merge(async1.await(), async2.await()) { first, second -> first to second }
	}
	
	private fun getFromDB() = combine(source1.getFromDB(), source2.getFromDB()) { first, second -> first to second }
	
	private suspend fun saveToDatabase(data: Pair<List<T1>, List<T2>>)
	{
		source1.saveToDB(data.first)
		source2.saveToDB(data.second)
	}
}
