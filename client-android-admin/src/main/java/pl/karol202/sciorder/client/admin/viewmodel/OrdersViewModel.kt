package pl.karol202.sciorder.client.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.client.admin.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.components.CoroutineViewModel
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.*
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.common.repository.resource.ResourceState
import pl.karol202.sciorder.common.model.Order

class OrdersViewModel(ownerDao: OwnerDao,
                      ownerApi: OwnerApi,
                      orderDao: OrderDao,
                      orderApi: OrderApi) : CoroutineViewModel()
{
	private val ownerRepository = OwnerRepositoryImpl(coroutineScope, ownerDao, ownerApi)
	private val orderRepository = OrderRepositoryImpl(coroutineScope, orderDao, orderApi)

	private val ownerLiveData = ownerRepository.getOwner()
	private val owner get() = ownerLiveData.value

	private val ordersResourceLiveData = ownerLiveData.nonNull().map { orderRepository.getAllOrders(it.id, it.hash) }
	private val ordersResourceAsLiveData = ordersResourceLiveData.switchMap { it.asLiveData }
	private val ordersResource get() = ordersResourceLiveData.value ?: EmptyResource<List<Order>>()

	val ordersLiveData = ordersResourceAsLiveData.map { it.data }
	val loadingLiveData = ordersResourceAsLiveData.map { it is ResourceState.Loading }
	val loadingErrorEventLiveData = ordersResourceAsLiveData.mapNotNull { if(it is ResourceState.Failure) Event(Unit) else null }

	private val _updateErrorEventLiveData = MediatorLiveData<Event<Unit>>()
	val updateErrorEventLiveData: LiveData<Event<Unit>> = _updateErrorEventLiveData

	private val _orderFilterLiveData = MutableLiveData(Order.Status.DEFAULTS)
	val orderFilterLiveData: LiveData<Set<Order.Status>> = _orderFilterLiveData
	var orderFilter: Set<Order.Status>
		get() = _orderFilterLiveData.value ?: emptySet()
		set(value) = _orderFilterLiveData.postValue(value)

	fun refreshOrders() = ordersResource.reload()

	fun updateOrderStatus(order: Order, status: Order.Status) = owner?.let { owner ->
		orderRepository.updateOrderStatus(owner, order, status).handleResponse()
	}

	fun removeAllOrders() = owner?.let { owner ->
		orderRepository.removeAllOrders(owner).handleResponse()
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse() = doOnFailure { _updateErrorEventLiveData.value = Event(Unit) }
}
