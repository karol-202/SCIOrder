package pl.karol202.sciorder.client.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.handleResponse
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.common.settings.Settings
import pl.karol202.sciorder.client.common.settings.liveString

class OwnerViewModel(private val ownerApi: OwnerApi,
                     settings: Settings) : ViewModel()
{
	enum class Error
	{
		NOT_FOUND, OTHER
	}

	private val _ownerIdSettingLiveData = settings.liveString("ownerId", null)
	val ownerIdSettingLiveData: LiveData<String?> = _ownerIdSettingLiveData
	private var ownerId: String?
		get() = _ownerIdSettingLiveData.value
		set(value) = _ownerIdSettingLiveData.postValue(value)

	private val _errorEventLiveData = MediatorLiveData<Event<Error>>()
	val errorEventLiveData: LiveData<Event<Error>> = _errorEventLiveData

	fun login(name: String) = ownerApi.getOwnerByName(name).handleResponse { ownerId = it.id }

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_errorEventLiveData,
			               successListener = successListener,
			               failureListener = { _errorEventLiveData.value = Event(it.toError()) })

	private fun ApiResponse.Error<*>.toError() = when(code)
	{
		OwnerApi.RESPONSE_NOT_FOUND -> Error.NOT_FOUND
		else -> Error.OTHER
	}
}
