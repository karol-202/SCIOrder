package pl.karol202.sciorder.client.android.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.DEFAULT_FILTER
import pl.karol202.sciorder.client.common.extensions.MutableLiveData
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.viewmodel.CoroutineViewModel
import pl.karol202.sciorder.common.Order

class OrdersViewModel(ownerRepository: OwnerRepository,
                      private val orderRepository: OrderRepository) : CoroutineViewModel()
{
	private val ownerFlow = ownerRepository.getOwner().conflate()

	private val ordersResourceFlow = ownerFlow.filterNotNull().map { orderRepository.getAllOrders(it.id, it.hash) }.conflate()
	private val ordersResourceAsFlow = ordersResourceFlow.switchMap { it.asFlow }

	val ordersLiveData = ordersResourceAsFlow.map { it.data }.asLiveData()
	val loadingLiveData = ordersResourceAsFlow.map { it is Resource.State.Loading }.asLiveData()
	val loadingErrorEventLiveData = ordersResourceAsFlow.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
														.asLiveData()

	private val _updateErrorEventLiveData = MutableLiveData<Event<Unit>>()
	val updateErrorEventLiveData: LiveData<Event<Unit>> = _updateErrorEventLiveData

	private val _orderFilterLiveData = MutableLiveData(Order.Status.DEFAULT_FILTER)
	val orderFilterLiveData: LiveData<Set<Order.Status>> = _orderFilterLiveData
	var orderFilter: Set<Order.Status>
		get() = _orderFilterLiveData.value ?: emptySet()
		set(value) = _orderFilterLiveData.postValue(value)

	fun refreshOrders() = launch { ordersResourceFlow.first().reload() }

	fun updateOrderStatus(order: Order, status: Order.Status) = launch {
		val owner = ownerFlow.first() ?: return@launch
		orderRepository.updateOrderStatus(owner, order, status).handleResponse()
	}

	fun removeAllOrders() = launch {
		val owner = ownerFlow.first() ?: return@launch
		orderRepository.removeAllOrders(owner).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() = ifFailure { _updateErrorEventLiveData.value = Event(Unit) }
}
