package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.model.DEFAULT_FILTER
import pl.karol202.sciorder.client.common.model.isLoggedAsAdmin
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Owner

abstract class OrdersViewModel(ownerRepository: OwnerRepository,
                               private val orderRepository: OrderRepository) : CoroutineViewModel()
{
	private var owner: Owner? = null
	private var ordersResource: Resource<List<Order>>? = null
		set(value)
		{
			field?.close()
			field = value
		}
	
	protected val orderFilterBroadcastChannel = ConflatedBroadcastChannel(Order.Status.DEFAULT_FILTER)

	private val ordersResourceAsBroadcastChannel = ownerRepository.getOwnerFlow()
													              .onEach { owner = it }
													              .filterNotNull()
													              .filter { it.isLoggedAsAdmin() }
													              .map { orderRepository.getOrdersResource(it.id, it.hash) }
													              .onEach { it.autoReloadIn(coroutineScope) }
													              .onEach { ordersResource = it }
													              .switchMap { it.asFlow }
																  .conflate()
													              .broadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)

	protected val unfilteredOrdersFlow = ordersResourceAsBroadcastChannel.asFlow().map { it.data }
	protected val ordersFlow = unfilteredOrdersFlow.combineLatest(orderFilterBroadcastChannel.asFlow()) { orders, filter ->
		orders?.filter { it.status in filter }
	}
	protected val loadingFlow = ordersResourceAsBroadcastChannel.asFlow().map { it is Resource.State.Loading }
	protected val loadingErrorEventFlow = ordersResourceAsBroadcastChannel.asFlow()
																		  .mapNotNull {if(it is Resource.State.Failure) Event(Unit) else null }

	protected val updateErrorEventBroadcastChannel = ConflatedBroadcastChannel<Event<Unit>>()

	var orderFilter: Set<Order.Status>
		get() = orderFilterBroadcastChannel.value
		set(value) { orderFilterBroadcastChannel.offer(value) }

	fun refreshOrders() = launch { ordersResource?.reload() }

	fun updateOrderStatus(order: Order, status: Order.Status) = launch {
		orderRepository.updateOrderStatus(owner ?: return@launch, order, status).handleResponse()
	}

	fun removeAllOrders() = launch {
		orderRepository.removeAllOrders(owner ?: return@launch).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() = ifFailure { updateErrorEventBroadcastChannel.offer(Event(Unit)) }

	override fun onCleared()
	{
		super.onCleared()
		ordersResource?.close()
		ordersResourceAsBroadcastChannel.cancel()
	}
}
