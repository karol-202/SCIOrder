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
	private val _ownerIdSettingLiveData = settings.liveString("ownerId", null)
	val ownerIdSettingLiveData: LiveData<String?> = _ownerIdSettingLiveData
	private var ownerId: String?
		get() = _ownerIdSettingLiveData.value
		set(value) = _ownerIdSettingLiveData.postValue(value)

	private val _errorEventLiveData = MediatorLiveData<Event<Unit>>()
	val errorEventLiveData: LiveData<Event<Unit>> = _errorEventLiveData

	fun login(name: String) = ownerApi.getOwnerByName(name).handleResponse { ownerId = it.id }

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_errorEventLiveData,
			               successListener = successListener,
			               failureListener = { _errorEventLiveData.value = Event(Unit) })
}
