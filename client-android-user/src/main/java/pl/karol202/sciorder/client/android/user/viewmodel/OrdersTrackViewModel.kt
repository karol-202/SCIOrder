package pl.karol202.sciorder.client.android.user.viewmodel

import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.viewmodel.CoroutineViewModel
import pl.karol202.sciorder.common.Order

class OrdersTrackViewModel(ownerRepository: OwnerRepository,
                           private val orderRepository: OrderTrackRepository) : CoroutineViewModel()
{
	private val ownerFlow = ownerRepository.getOwner().conflate()

	private val ordersResourceFlow = ownerFlow.filterNotNull().map { orderRepository.getTrackedOrders(it.id) }
	private val ordersResourceAsLiveData = ordersResourceFlow.switchMap { it.asFlow }

	val ordersLiveData = ordersResourceAsLiveData.map { it.data }.asLiveData()
	val loadingLiveData = ordersResourceAsLiveData.map { it is Resource.State.Loading }.asLiveData()
	val errorEventLiveData = ordersResourceAsLiveData.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
													 .asLiveData()

	fun refreshOrders() = launch { ordersResourceFlow.first().reload() }

	fun removeOrder(order: Order) = launch { orderRepository.removeOrder(order) }
}
