package pl.karol202.sciorder.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.model.remote.ApiResponse

abstract class Resource<T>
{
	private val liveData = MediatorLiveData<ResourceState<T>>().apply { value = ResourceState.Loading(null) }

	val asLiveData: LiveData<ResourceState<T>> = liveData

	init
	{
		@Suppress("LeakingThis")
		val databaseSource = loadFromDatabase()
		liveData.addSource(databaseSource) { data ->
			liveData.removeSource(databaseSource)
			if(shouldFetchFromNetwork(data)) fetchFromNetwork(databaseSource)
			else addDatabaseAsSource(databaseSource)
		}
	}

	private fun fetchFromNetwork(databaseSource: LiveData<T>)
	{
		liveData.addSource(databaseSource) { data ->
			liveData.removeSource(databaseSource)
			setLoading(data)
		}

		val networkSource = loadFromNetwork()
		liveData.addSource(networkSource) { response ->
			liveData.removeSource(networkSource)
			liveData.removeSource(databaseSource)
			when(response)
			{
				is ApiResponse.ApiResponseSuccess -> onNetworkFetchSuccess(databaseSource, response)
				is ApiResponse.ApiResponseError -> onNetworkFetchFailure(databaseSource, response)
			}
		}
	}

	private fun onNetworkFetchSuccess(databaseSource: LiveData<T>, response: ApiResponse.ApiResponseSuccess<T>)
	{
		saveToDatabase(response.data)
		addDatabaseAsSource(databaseSource)
	}

	private fun onNetworkFetchFailure(databaseSource: LiveData<T>, response: ApiResponse.ApiResponseError<T>)
	{
		liveData.addSource(databaseSource) { data ->
			setFailure(data, response.message)
		}
	}

	private fun addDatabaseAsSource(databaseSource: LiveData<T>)
	{
		liveData.addSource(databaseSource) { data -> setSuccess(data) }
	}

	private fun setSuccess(data: T) = ResourceState.Success(data)

	private fun setLoading(data: T?) = ResourceState.Loading(data)

	private fun setFailure(data: T?, message: String) = ResourceState.Failure(data, message)

	protected abstract fun shouldFetchFromNetwork(data: T): Boolean

	protected abstract fun loadFromDatabase(): LiveData<T>

	protected abstract fun loadFromNetwork(): LiveData<ApiResponse<T>>

	protected abstract fun saveToDatabase(data: T)
}