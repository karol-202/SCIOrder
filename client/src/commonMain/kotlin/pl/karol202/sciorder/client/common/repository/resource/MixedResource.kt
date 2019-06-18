package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import pl.karol202.sciorder.client.common.extensions.tryWithLockSuspend
import pl.karol202.sciorder.client.common.model.remote.ApiResponse

// LiveData can only be modified from main thread
abstract class MixedResource<T>(protected val databaseSource: Flow<T>) : Resource<T>
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

	private var fetchingMutex = Mutex()

	private val stateFlow = ConflatedBroadcastChannel<State>(State.Success)
	override val asFlow = databaseSource.onEach { if(shouldFetchFromNetwork(databaseSource.first())) reload() }
										.combineLatest(stateFlow.asFlow()) { data, state -> state.toResourceState(data) }

	override suspend fun reload()
	{
		println("$this@reload")
		fetchingMutex.tryWithLockSuspend { executeReload() }
	}

	private suspend fun executeReload()
	{
		println("$this@executeReload")
		stateFlow.send(State.Loading)
		val response = loadFromNetwork(databaseSource.first())
		response.ifSuccess { saveToDatabase(it) }
		stateFlow.send(State.fromApiResponse(response))
	}

	protected abstract fun shouldFetchFromNetwork(data: T): Boolean

	protected abstract suspend fun loadFromNetwork(oldData: T): ApiResponse<T>

	protected abstract suspend fun saveToDatabase(data: T)
}
