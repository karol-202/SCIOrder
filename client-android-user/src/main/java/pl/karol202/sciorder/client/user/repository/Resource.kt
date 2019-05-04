package pl.karol202.sciorder.client.user.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.client.user.model.remote.ApiResponse

abstract class Resource<T> @MainThread constructor() // LiveData can only be modified from main thread
{
	@Suppress("LeakingThis")
	private val databaseSource = loadFromDatabase()

	private val liveData = MediatorLiveData<ResourceState<T>>().apply { value = ResourceState.Loading(null) }
	val asLiveData: LiveData<ResourceState<T>> = liveData

	private var fetching = false

	init
	{
		liveData.addSource(databaseSource) { data ->
			liveData.removeSource(databaseSource)
			if(shouldFetchFromNetwork(data)) fetchFromNetwork()
			else liveData.addSource(databaseSource) { setSuccess(it) }
		}
	}

	@MainThread // LiveData can only be modified from main thread
	fun reload()
	{
		if(fetching) return
		liveData.removeSource(databaseSource)
		fetchFromNetwork()
	}

	private fun fetchFromNetwork()
	{
		fetching = true

		liveData.addSource(databaseSource) { data ->
			liveData.removeSource(databaseSource)
			setLoading(data)

			val networkSource = loadFromNetwork(data)
			liveData.addSource(networkSource) { response ->
				liveData.removeSource(networkSource)
				liveData.removeSource(databaseSource)
				when(response)
				{
					is ApiResponse.Success -> onNetworkFetchSuccess(response)
					is ApiResponse.Error -> onNetworkFetchFailure(response)
				}
				fetching = false
			}
		}
	}

	private fun onNetworkFetchSuccess(response: ApiResponse.Success<T>)
	{
		saveToDatabase(response.data)
		liveData.addSource(databaseSource) { data -> setSuccess(data) }
	}

	private fun onNetworkFetchFailure(response: ApiResponse.Error<T>)
	{
		liveData.addSource(databaseSource) { data -> setFailure(data, response.message) }
	}

	private fun setSuccess(data: T) = liveData.postValue(ResourceState.Success(data))

	private fun setLoading(data: T?) = liveData.postValue(ResourceState.Loading(data))

	private fun setFailure(data: T?, message: String) = liveData.postValue(ResourceState.Failure(data, message))

	protected abstract fun shouldFetchFromNetwork(data: T): Boolean

	protected abstract fun loadFromDatabase(): LiveData<T>

	// oldData needed by OrderRepositoryImpl
	protected abstract fun loadFromNetwork(oldData: T): LiveData<ApiResponse<T>>

	protected abstract fun saveToDatabase(data: T)
}
