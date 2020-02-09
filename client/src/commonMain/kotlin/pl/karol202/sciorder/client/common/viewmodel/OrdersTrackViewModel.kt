package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.model.Order

abstract class OrdersTrackViewModel(ownerRepository: OwnerRepository,
                                    private val orderRepository: OrderTrackRepository) : ViewModel()
{
	private var ordersResource: Resource<List<Order>>? = null
		set(value)
		{
			field?.close()
			field = value
		}

	private val ordersStateBroadcastChannel = ownerRepository.getOwnerFlow()
															 .map { it?.let { orderRepository.getTrackedOrdersResource(it.id) } }
															 .onEach { ordersResource = it }
															 .onEach { it?.autoReloadIn(coroutineScope) }
															 .flatMapLatest { it?.asFlow ?: flowOf(null) }
															 .conflate()
															 .broadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)
	
	private val ordersStateFlow = ordersStateBroadcastChannel.asFlow()

	protected val ordersFlow = ordersStateFlow.map { it?.data.orEmpty() }
											  .distinctUntilChanged()
	protected val loadingFlow = ordersStateFlow.map { it is Resource.State.Loading }
											   .distinctUntilChanged()
	protected val errorEventFlow = ordersStateFlow.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
												  .distinctUntilChanged()

	fun refreshOrders() = launch { ordersResource?.reload() }

	fun removeOrder(order: Order) = launch { orderRepository.removeOrder(order) }

	override fun onCleared()
	{
		super.onCleared()
		ordersResource?.close()
		ordersStateBroadcastChannel.cancel()
	}
}
