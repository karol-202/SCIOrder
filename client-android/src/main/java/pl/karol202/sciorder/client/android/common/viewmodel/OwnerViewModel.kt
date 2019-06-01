package pl.karol202.sciorder.client.android.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.client.android.common.components.CoroutineViewModel
import pl.karol202.sciorder.client.android.common.components.Event
import pl.karol202.sciorder.client.android.common.extensions.doOnFailure
import pl.karol202.sciorder.client.android.common.extensions.sha1
import pl.karol202.sciorder.client.android.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.android.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.android.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.android.common.repository.owner.OwnerRepositoryImpl

abstract class OwnerViewModel(ownerDao: OwnerDao,
                              ownerApi: OwnerApi) : CoroutineViewModel()
{
	enum class Error
	{
		NOT_FOUND, NAME_BUSY, OTHER
	}

	private val ownerRepository = OwnerRepositoryImpl(coroutineScope, ownerDao, ownerApi)

	val ownerLiveData = ownerRepository.getOwner()

	private val _errorEventLiveData = MediatorLiveData<Event<Error>>()
	val errorEventLiveData: LiveData<Event<Error>> = _errorEventLiveData

	fun login(name: String, password: String? = null) =
			ownerRepository.login(name, password?.sha1()).handleResponse()

	fun register(name: String, password: String) =
			ownerRepository.register(name, password.sha1()).handleResponse()

	private fun <T> LiveData<ApiResponse<T>>.handleResponse() =
			doOnFailure { _errorEventLiveData.value = Event(it.toError()) }

	private fun ApiResponse.Error<*>.toError() = when(code)
	{
		OwnerApi.RESPONSE_NOT_FOUND -> Error.NOT_FOUND
		OwnerApi.RESPONSE_CONFLICT -> Error.NAME_BUSY
		else -> Error.OTHER
	}

	fun logout()
	{
		ownerRepository.logout()
		onLogout()
	}

	open fun onLogout() {}
}
