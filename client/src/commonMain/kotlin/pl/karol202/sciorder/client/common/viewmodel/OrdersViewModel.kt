package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.model.DEFAULT_FILTER
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
	
	private val ordersStateBroadcastChannel = ownerRepository.getOwnerFlow()
															 .onEach { owner = it }
															 .map { it?.let { orderRepository.getOrdersResource(it.id, it.hash) } }
															 .onEach { it?.autoReloadIn(coroutineScope) }
															 .onEach { ordersResource = it }
															 .flatMapLatest { it?.asFlow ?: flowOf(null) }
															 .conflate()
															 .broadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)
	private val orderFilterBroadcastChannel = ConflatedBroadcastChannel(Order.Status.DEFAULT_FILTER)
	private val updateErrorEventBroadcastChannel = ConflatedBroadcastChannel<Event<Unit>>()
	
	private val ordersStateFlow = ordersStateBroadcastChannel.asFlow()
	private val rawOrdersFlow = ordersStateFlow.map { it?.data.orEmpty() }
	
	protected val filterFlow = orderFilterBroadcastChannel.asFlow()
	protected val anyOrdersPresentFlow = rawOrdersFlow.map { it.isNotEmpty() }
													  .distinctUntilChanged()
	protected val ordersFlow = rawOrdersFlow.combine(filterFlow) { orders, filter -> orders.filter { it.status in filter } }
											.distinctUntilChanged()
	protected val loadingFlow = ordersStateFlow.map { it is Resource.State.Loading }
											   .distinctUntilChanged()
	protected val loadingErrorEventFlow = ordersStateFlow.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
														 .distinctUntilChanged()
	protected val updateErrorEventFlow = updateErrorEventBroadcastChannel.asFlow()
																		 .distinctUntilChanged()
	
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
		ordersStateBroadcastChannel.cancel()
		orderFilterBroadcastChannel.cancel()
		updateErrorEventBroadcastChannel.cancel()
	}
}
