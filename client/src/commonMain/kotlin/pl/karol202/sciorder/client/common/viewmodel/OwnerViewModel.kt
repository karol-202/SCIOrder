package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.sha1

abstract class OwnerViewModel(private val ownerRepository: OwnerRepository) : CoroutineViewModel()
{
	enum class Error
	{
		NOT_FOUND, NAME_BUSY, OTHER
	}

	protected val ownerFlow = ownerRepository.getOwnerFlow()

	protected val errorEventBroadcastChannel = ConflatedBroadcastChannel<Event<Error>>()

	fun login(name: String, password: String? = null) = launch {
		ownerRepository.login(name, password?.sha1()).handleResponse()
	}

	fun register(name: String, password: String) = launch {
		ownerRepository.register(name, password.sha1()).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() = ifFailure { errorEventBroadcastChannel.offer(Event(it.toError())) }

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

	protected abstract fun onLogout()
}
