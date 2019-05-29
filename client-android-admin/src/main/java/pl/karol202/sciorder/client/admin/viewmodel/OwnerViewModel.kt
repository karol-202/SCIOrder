package pl.karol202.sciorder.client.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.create
import pl.karol202.sciorder.client.common.extensions.handleResponse
import pl.karol202.sciorder.client.common.extensions.sha1
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.common.settings.Settings
import pl.karol202.sciorder.client.common.settings.liveString
import pl.karol202.sciorder.common.model.Owner

class OwnerViewModel(private val ownerApi: OwnerApi,
                     settings: Settings) : ViewModel()
{
	enum class Error
	{
		NOT_FOUND, NAME_BUSY, OTHER
	}

	private val _ownerIdSettingLiveData = settings.liveString("ownerId", null)
	val ownerIdSettingLiveData: LiveData<String?> = _ownerIdSettingLiveData
	private var ownerId: String?
		get() = _ownerIdSettingLiveData.value
		set(value) = _ownerIdSettingLiveData.postValue(value)

	private val _hashSettingLiveData = settings.liveString("hash", null)
	private var hash: String?
		get() = _hashSettingLiveData.value
		set(value) = _hashSettingLiveData.postValue(value)

	private val _errorEventLiveData = MediatorLiveData<Event<Error>>()
	val errorEventLiveData: LiveData<Event<Error>> = _errorEventLiveData

	fun login(name: String, password: String) =
			ownerApi.getOwnerByName(name, password.sha1()).handleResponse { saveOwner(it) }

	fun register(name: String, password: String) =
			ownerApi.addOwner(Owner.create(name, password.sha1())).handleResponse { saveOwner(it) }

	private fun saveOwner(owner: Owner)
	{
		ownerId = owner.id
		hash = owner.hash
	}

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
