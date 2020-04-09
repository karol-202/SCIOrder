package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.model.OrderWithProducts
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event

abstract class UserOrdersViewModel(userAuthRepository: UserAuthRepository,
                                   private val orderRepository: OrderRepository) : ViewModel()
{
	private val userAuthFlow = userAuthRepository.getUserAuthFlow()
	
	private var ordersResource: Resource<List<OrderWithProducts>>? = null
	private val ordersResourceChannel = userAuthFlow
			.map { it?.let { orderRepository.getOrdersResource(it.authToken, it.store.id) } }
			.scan(null as Resource<List<OrderWithProducts>>?) { previous, current -> previous?.close(); current }
			.onEach { ordersResource = it }
			.onEach { it?.autoReloadIn(coroutineScope) }
			.conflate()
			.broadcastIn(coroutineScope, CoroutineStart.DEFAULT)
	
	private val ordersStateFlow = ordersResourceChannel
			.asFlow()
			.flatMapLatest { it?.asFlow ?: flowOf(null) }

	protected val ordersFlow = ordersStateFlow
			.map { it?.data.orEmpty() }
			.distinctUntilChanged()
	
	protected val loadingFlow = ordersStateFlow
			.map { it is Resource.State.Loading }
			.distinctUntilChanged()
	
	protected val errorEventFlow = ordersStateFlow
			.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
			.distinctUntilChanged()

	fun refreshOrders() = launch { ordersResource?.reload() }

	override fun onCleared() = launch {
		super.onCleared()
		ordersResource?.close()
		ordersResourceChannel.cancel()
	}
}
