package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.model.create
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.sha1
import pl.karol202.sciorder.common.model.Owner

abstract class OwnerViewModel(private val ownerRepository: OwnerRepository) : ViewModel()
{
	enum class Error
	{
		NOT_FOUND, NAME_BUSY, NAME_INVALID, PASSWORD_TOO_SHORT, OTHER
	}
	
	companion object
	{
		private const val MIN_PASSWORD_LENGTH = 3
	}

	private val errorEventBroadcastChannel = ConflatedBroadcastChannel<Event<Error>>()
	
	protected val ownerFlow = ownerRepository.getOwnerFlow()
	protected val errorEventFlow = errorEventBroadcastChannel.asFlow()

	fun login(name: String, password: String? = null) = launch {
		ownerRepository.login(name, password?.sha1()).handleResponse()
	}

	fun register(name: String, password: String) = launch {
		val owner = Owner.create(name, password.sha1())
		when
		{
			!owner.isNameValid -> broadcastError(Error.NAME_INVALID)
			password.length < MIN_PASSWORD_LENGTH -> broadcastError(Error.PASSWORD_TOO_SHORT)
			owner.isValid -> ownerRepository.register(owner).handleResponse()
			else -> throw IllegalStateException()
		}
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() = ifFailure { broadcastError(it.toError()) }
	
	private fun broadcastError(error: Error)
	{
		errorEventBroadcastChannel.offer(Event(error))
	}

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
	
	override fun onCleared()
	{
		super.onCleared()
		errorEventBroadcastChannel.cancel()
	}
}
