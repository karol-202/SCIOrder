package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.util.tryDoLocking

abstract class AbstractResource<T>(databaseFlow: Flow<T>) : Resource<T>
{
	private sealed class StateType
	{
		object Success : StateType()
		{
			override fun <T> toResourceState(data: T) = Resource.State.Success(data)
		}

		object Loading : StateType()
		{
			override fun <T> toResourceState(data: T) = Resource.State.Loading(data)
		}

		data class Failure(val type: Resource.State.Failure.Type) : StateType()
		{
			override fun <T> toResourceState(data: T) = Resource.State.Failure(data, type)
		}

		companion object
		{
			fun <T> fromApiResponse(apiResponse: ApiResponse<T>) = when(apiResponse)
			{
				is ApiResponse.Success -> Success
				is ApiResponse.Error -> Failure(Resource.State.Failure.Type.fromApiResponseType(apiResponse.type))
			}
		}

		abstract fun <T> toResourceState(data: T): Resource.State<T>
	}

	private var reloadMutex = Mutex()
	private var autoReloadJob: Job? = null

	private val stateChannel = ConflatedBroadcastChannel<StateType>(StateType.Success)
	override val asFlow = databaseFlow
			.combine(stateChannel.asFlow()) { data, state -> state.toResourceState(data) }
			.distinctUntilChanged()

	override suspend fun autoReloadIn(coroutineScope: CoroutineScope)
	{
		autoReloadJob?.cancel()
		autoReloadJob = coroutineScope.launch { autoReload() }
	}
	
	private suspend fun autoReload()
	{
		while(true)
		{
			reload()
			waitForNextUpdate()
		}
	}

	protected abstract suspend fun waitForNextUpdate()

	override suspend fun reload() = reloadMutex.tryDoLocking { executeReload() }

	private suspend fun executeReload()
	{
		stateChannel.send(StateType.Loading)
		val response = loadFromApi()
		response.ifSuccess { saveToDatabase(it) }
		stateChannel.send(StateType.fromApiResponse(response))
	}

	protected abstract suspend fun loadFromApi(): ApiResponse<T>

	protected abstract suspend fun saveToDatabase(data: T)

	override suspend fun close() = reloadMutex.withLock {
		autoReloadJob?.cancel()
		stateChannel.cancel()
	}
}
