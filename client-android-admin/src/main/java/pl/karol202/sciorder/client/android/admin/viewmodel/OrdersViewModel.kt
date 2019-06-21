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
import pl.karol202.sciorder.common.Owner

class OrdersViewModel(ownerRepository: OwnerRepository,
                      private val orderRepository: OrderRepository) : CoroutineViewModel()
{
	private var owner: Owner? = null
	private var ordersResource: Resource<List<Order>>? = null
		set(value)
		{
			field?.close()
			field = value
		}

	private val ordersResourceAsBroadcastChannel = ownerRepository.getOwnerFlow()
													              .onEach { owner = it }
													              .filterNotNull()
													              .map { orderRepository.getOrdersResource(it.id, it.hash) }
													              .onEach { it.autoReloadIn(coroutineScope) }
													              .onEach { ordersResource = it }
													              .switchMap { it.asFlow }
																  .conflate()
													              .broadcastIn(coroutineScope)

	val ordersLiveData = ordersResourceAsBroadcastChannel.asFlow().map { it.data }.asLiveData()
	val loadingLiveData = ordersResourceAsBroadcastChannel.asFlow().map { it is Resource.State.Loading }.asLiveData()
	val loadingErrorEventLiveData = ordersResourceAsBroadcastChannel.asFlow()
																	.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
																	.asLiveData()

	private val _updateErrorEventLiveData = MutableLiveData<Event<Unit>>()
	val updateErrorEventLiveData: LiveData<Event<Unit>> = _updateErrorEventLiveData

	private val _orderFilterLiveData = MutableLiveData(Order.Status.DEFAULT_FILTER)
	val orderFilterLiveData: LiveData<Set<Order.Status>> = _orderFilterLiveData
	var orderFilter: Set<Order.Status>
		get() = _orderFilterLiveData.value ?: emptySet()
		set(value) = _orderFilterLiveData.postValue(value)

	fun refreshOrders() = launch { ordersResource?.reload() }

	fun updateOrderStatus(order: Order, status: Order.Status) = launch {
		orderRepository.updateOrderStatus(owner ?: return@launch, order, status).handleResponse()
	}

	fun removeAllOrders() = launch {
		orderRepository.removeAllOrders(owner ?: return@launch).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() = ifFailure { _updateErrorEventLiveData.postValue(Event(Unit)) }

	override fun onCleared()
	{
		super.onCleared()
		ordersResource?.close()
		ordersResourceAsBroadcastChannel.cancel()
	}
}
