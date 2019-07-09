package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.Order

abstract class OrdersTrackViewModel(ownerRepository: OwnerRepository,
                                    private val orderRepository: OrderTrackRepository) : CoroutineViewModel()
{
	private var ordersResource: Resource<List<Order>>? = null
		set(value)
		{
			field?.close()
			field = value
		}

	private val ordersResourceAsBroadcastChannel = ownerRepository.getOwnerFlow()
																  .filterNotNull()
																  .map { orderRepository.getTrackedOrdersResource(it.id) }
																  .onEach { ordersResource = it }
																  .onEach { it.autoReloadIn(coroutineScope) }
																  .switchMap { it.asFlow }
																  .conflate()
																  .broadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)

	protected val ordersFlow = ordersResourceAsBroadcastChannel.asFlow().map { it.data }
	protected val loadingFlow = ordersResourceAsBroadcastChannel.asFlow().map { it is Resource.State.Loading }
	protected val errorEventFlow = ordersResourceAsBroadcastChannel.asFlow()
															 .mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }

	fun refreshOrders() = launch { ordersResource?.reload() }

	fun removeOrder(order: Order) = launch { orderRepository.removeOrder(order) }

	override fun onCleared()
	{
		super.onCleared()
		ordersResource?.close()
		ordersResourceAsBroadcastChannel.cancel()
	}
}
