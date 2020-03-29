package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.product.parameter.ProductParameterRepository
import pl.karol202.sciorder.client.common.repository.user.UserRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.generatePassword
import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest

abstract class UserLoginViewModel(private val userRepository: UserRepository,
                                  private val userAuthRepository: UserAuthRepository,
                                  private val orderRepository: OrderRepository,
                                  private val productRepository: ProductRepository,
                                  private val productParameterRepository: ProductParameterRepository) : ViewModel()
{
	enum class Error
	{
		NETWORK, CANNOT_LOGIN, OTHER
	}
	
	private val errorEventChannel = ConflatedBroadcastChannel<Event<Error>>()
	
	private val userFlow = userRepository.getUserFlow()
	private val userAuthFlow = userAuthRepository.getUserAuthFlow()
	
	protected val storeFlow = userAuthFlow
			.map { it?.store }
	
	protected val errorEventFlow = errorEventChannel
			.asFlow()
			.distinctUntilChanged()
	
	init
	{
		registerIfNoUser()
	}
	
	private fun registerIfNoUser() = launch {
		if(userFlow.first() == null) register()
	}
	
	private fun register() = launch {
		val request = UserRequest(generatePassword())
		userRepository.registerUser(request).handleRegisterError()
	}
	
	fun login(storeName: String) = launch {
		val user = userFlow.first() ?: return@launch
		val request = UserLoginRequest(user.id, user.password, storeName)
		userAuthRepository.login(request).handleLoginError()
	}
	
	private suspend fun <T> ApiResponse<T>.handleRegisterError() =
			ifFailure { errorEventChannel.offer(Event(it.mapError())) }
	
	private suspend fun <T> ApiResponse<T>.handleLoginError() =
			ifFailure { errorEventChannel.offer(Event(it.mapError())) }
	
	private fun ApiResponse.Error.mapError() = when(type)
	{
		ApiResponse.Error.Type.NETWORK -> Error.NETWORK
		ApiResponse.Error.Type.FORBIDDEN -> Error.CANNOT_LOGIN
		else -> Error.OTHER
	}
	
	fun logout() = launch {
		userAuthRepository.logout()
		orderRepository.cleanLocalOrders()
		productRepository.cleanLocalProducts()
	}
}
