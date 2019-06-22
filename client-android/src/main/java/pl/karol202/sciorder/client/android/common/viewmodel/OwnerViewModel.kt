package pl.karol202.sciorder.client.android.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.util.Event
import pl.karol202.sciorder.client.android.common.util.sha1
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository

abstract class OwnerViewModel(private val ownerRepository: OwnerRepository) : CoroutineViewModel()
{
	enum class Error
	{
		NOT_FOUND, NAME_BUSY, OTHER
	}

	val ownerLiveData = ownerRepository.getOwnerFlow().asLiveData()

	private val _errorEventLiveData = MutableLiveData<Event<Error>>()
	val errorEventLiveData: LiveData<Event<Error>> = _errorEventLiveData

	fun login(name: String, password: String? = null) = coroutineScope.launch {
		ownerRepository.login(name, password?.sha1()).handleResponse()
	}

	fun register(name: String, password: String) = coroutineScope.launch {
		ownerRepository.register(name, password.sha1()).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() = ifFailure { _errorEventLiveData.postValue(Event(it.toError())) }

	private fun ApiResponse.Error.toError() = when(type)
	{
		ApiResponse.Error.Type.NOT_FOUND -> Error.NOT_FOUND
		ApiResponse.Error.Type.CONFLICT -> Error.NAME_BUSY
		else -> Error.OTHER
	}

	fun logout() = launch {
		ownerRepository.logout()
		onLogout()
	}

	open fun onLogout() {}
}
