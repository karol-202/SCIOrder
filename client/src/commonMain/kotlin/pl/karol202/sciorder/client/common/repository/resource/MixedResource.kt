package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combineLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.util.tryDoLocking

abstract class MixedResource<T>(protected val databaseFlow: Flow<T>) : Resource<T>
{
	private sealed class State
	{
		object Success : State()
		{
			override fun <T> toResourceState(data: T) = Resource.State.Success(data)
		}

		object Loading : State()
		{
			override fun <T> toResourceState(data: T) = Resource.State.Loading(data)
		}

		data class Failure(val type: Resource.State.Failure.Type) : State()
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

	private val stateChannel = ConflatedBroadcastChannel<State>(State.Success)
	override val asFlow = databaseFlow.combineLatest(stateChannel.asFlow()) { data, state -> state.toResourceState(data) }

	override suspend fun autoReloadIn(coroutineScope: CoroutineScope)
	{
		autoReloadJob?.cancel()
		autoReloadJob = coroutineScope.launch {
			while(true)
			{
				reload()
				waitForNextUpdate()
			}
		}
	}

	protected abstract suspend fun waitForNextUpdate()

	override suspend fun reload() = reloadMutex.tryDoLocking { executeReload() }

	private suspend fun executeReload()
	{
		stateChannel.send(State.Loading)
		val response = loadFromNetwork(databaseFlow.first())
		response.ifSuccess { saveToDatabase(it) }
		stateChannel.send(State.fromApiResponse(response))
	}

	protected abstract suspend fun loadFromNetwork(oldData: T): ApiResponse<T>

	protected abstract suspend fun saveToDatabase(data: T)

	override fun close()
	{
		stateChannel.cancel()
		autoReloadJob?.cancel()
	}
}
