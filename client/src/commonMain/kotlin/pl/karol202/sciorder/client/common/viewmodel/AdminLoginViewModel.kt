package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.admin.AdminRepository
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.sendNow
import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest
import pl.karol202.sciorder.common.validation.isNameValid
import pl.karol202.sciorder.common.validation.isPasswordValid

abstract class AdminLoginViewModel(private val adminRepository: AdminRepository,
                                   private val adminAuthRepository: AdminAuthRepository,
                                   private val storeRepository: StoreRepository,
                                   private val orderRepository: OrderRepository,
                                   private val productRepository: ProductRepository) : ViewModel()
{
	enum class Error
	{
		NETWORK, CANNOT_LOGIN, NAME_BUSY, NAME_INVALID, PASSWORD_INVALID, OTHER
	}
	
	private val errorEventChannel = ConflatedBroadcastChannel<Event<Error>>()
	
	private val adminAuthFlow = adminAuthRepository.getAdminAuthFlow()
	
	protected val adminFlow = adminAuthFlow
			.map { it?.admin }
	
	protected val errorEventFlow = errorEventChannel
			.asFlow()
			.distinctUntilChanged()
	
	fun register(name: String, password: String) = launch {
		val request = AdminRequest(name, password)
		when
		{
			!request.isNameValid -> broadcastError(Error.NAME_INVALID)
			!request.isPasswordValid -> broadcastError(Error.PASSWORD_INVALID)
			else -> adminRepository.registerAdmin(request).handleRegisterError().ifSuccess { login(name, password) }
		}
	}
	
	fun login(name: String, password: String) = launch {
		val request = AdminLoginRequest(name, password)
		adminAuthRepository.login(request).handleLoginError()
	}
	
	private suspend fun <T> ApiResponse<T>.handleRegisterError() =
			ifFailure { broadcastError(it.mapRegisterError()) }
	
	private fun ApiResponse.Error.mapRegisterError() = when(type)
	{
		ApiResponse.Error.Type.NETWORK -> Error.NETWORK
		ApiResponse.Error.Type.CONFLICT -> Error.NAME_BUSY
		else -> Error.OTHER
	}
	
	private suspend fun <T> ApiResponse<T>.handleLoginError() = ifFailure { broadcastError(it.mapLoginError()) }
	
	private fun ApiResponse.Error.mapLoginError() = when(type)
	{
		ApiResponse.Error.Type.NETWORK -> Error.NETWORK
		ApiResponse.Error.Type.FORBIDDEN -> Error.CANNOT_LOGIN
		else -> Error.OTHER
	}
	
	private fun broadcastError(error: Error) = errorEventChannel.sendNow(Event(error))
	
	fun logout() = launch {
		adminAuthRepository.logout()
		storeRepository.cleanLocalStores()
		orderRepository.cleanLocalOrders()
		productRepository.cleanLocalProducts()
	}
}
