package pl.karol202.sciorder.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.model.remote.ApiResponse

abstract class Resource<T> @MainThread constructor()
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

	@MainThread
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
		}

		val networkSource = loadFromNetwork()
		liveData.addSource(networkSource) { response ->
			liveData.removeSource(networkSource)
			liveData.removeSource(databaseSource)
			when(response)
			{
				is ApiResponse.ApiResponseSuccess -> onNetworkFetchSuccess(response)
				is ApiResponse.ApiResponseError -> onNetworkFetchFailure(response)
			}
			fetching = false
		}
	}

	private fun onNetworkFetchSuccess(response: ApiResponse.ApiResponseSuccess<T>)
	{
		saveToDatabase(response.data)
		liveData.addSource(databaseSource) { data -> setSuccess(data) }
	}

	private fun onNetworkFetchFailure(response: ApiResponse.ApiResponseError<T>)
	{
		liveData.addSource(databaseSource) { data -> setFailure(data, response.message) }
	}

	private fun setSuccess(data: T) = liveData.postValue(ResourceState.Success(data))

	private fun setLoading(data: T?) = liveData.postValue(ResourceState.Loading(data))

	private fun setFailure(data: T?, message: String) = liveData.postValue(ResourceState.Failure(data, message))

	protected abstract fun shouldFetchFromNetwork(data: T): Boolean

	protected abstract fun loadFromDatabase(): LiveData<T>

	protected abstract fun loadFromNetwork(): LiveData<ApiResponse<T>>

	protected abstract fun saveToDatabase(data: T)
}