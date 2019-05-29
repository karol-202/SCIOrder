package pl.karol202.sciorder.client.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.components.CoroutineViewModel
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.create
import pl.karol202.sciorder.client.common.extensions.handleResponse
import pl.karol202.sciorder.client.common.extensions.sha1
import pl.karol202.sciorder.client.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.common.model.Owner

class OwnerViewModel(private val ownerDao: OwnerDao,
                     private val ownerApi: OwnerApi) : CoroutineViewModel()
{
	enum class Error
	{
		NOT_FOUND, NAME_BUSY, OTHER
	}

	val ownerLiveData = ownerDao.get()
	private var owner: Owner?
		get() = ownerLiveData.value
		set(value) { launch { ownerDao.set(value) } }

	private val _errorEventLiveData = MediatorLiveData<Event<Error>>()
	val errorEventLiveData: LiveData<Event<Error>> = _errorEventLiveData

	fun logout() = run { owner = null }

	fun login(name: String, password: String? = null) =
			ownerApi.getOwnerByName(name, password?.sha1()).handleResponse { owner = it }

	fun register(name: String, password: String) =
			ownerApi.addOwner(Owner.create(name, password.sha1())).handleResponse { owner = it }

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_errorEventLiveData,
			               successListener = successListener,
			               failureListener = { _errorEventLiveData.value = Event(it.toError()) })

	private fun ApiResponse.Error<*>.toError() = when(code)
	{
		OwnerApi.RESPONSE_NOT_FOUND -> Error.NOT_FOUND
		OwnerApi.RESPONSE_CONFLICT -> Error.NAME_BUSY
		else -> Error.OTHER
	}
}
