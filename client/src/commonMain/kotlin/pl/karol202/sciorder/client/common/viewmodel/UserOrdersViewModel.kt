package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.conflatedBroadcastIn
import pl.karol202.sciorder.common.model.Order

abstract class UserOrdersViewModel(userAuthRepository: UserAuthRepository,
                                   private val orderRepository: OrderRepository) : ViewModel()
{
	private val userAuthFlow = userAuthRepository.getUserAuthFlow()
	
	private val ordersResourceChannel = userAuthFlow
			.map { it?.let { orderRepository.getOrdersResource(it.authToken, it.store.id) } }
			.scan(null as Resource<List<Order>>?) { previous, current -> previous?.close(); current }
			.onEach { it?.autoReloadIn(coroutineScope) }
			.conflatedBroadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)
	
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

	fun refreshOrders() = launch { ordersResourceChannel.valueOrNull?.reload() }

	override fun onCleared()
	{
		super.onCleared()
		ordersResourceChannel.valueOrNull?.close()
		ordersResourceChannel.close()
	}
}
