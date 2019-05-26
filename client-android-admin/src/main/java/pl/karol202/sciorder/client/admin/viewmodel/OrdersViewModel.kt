package pl.karol202.sciorder.client.admin.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.admin.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.handleResponse
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.repository.ResourceState
import pl.karol202.sciorder.common.model.Order

class OrdersViewModel(private val orderDao: OrderDao,
                      private val orderApi: OrderApi) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val ordersRepository = OrderRepositoryImpl(coroutineScope, orderDao, orderApi)
	private val ordersResource = ordersRepository.getAllOrders()

	val ordersLiveData: LiveData<List<Order>> = Transformations.map(ordersResource.asLiveData) { it.data }
	val loadingLiveData: LiveData<Boolean> = Transformations.map(ordersResource.asLiveData) { it is ResourceState.Loading }
	val loadingErrorEventLiveData: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>().apply {
		addSource(ordersResource.asLiveData) { resourceState ->
			if(resourceState is ResourceState.Failure) value = Event(Unit)
		}
	}

	private val _updateErrorEventLiveData = MediatorLiveData<Event<Unit>>()
	val updateErrorEventLiveData: LiveData<Event<Unit>> = _updateErrorEventLiveData

	private val _orderFilterLiveData = MutableLiveData<Set<Order.Status>>().apply {
		value = Order.Status.values().toSet() - Order.Status.DONE - Order.Status.REJECTED
	}
	val orderFilterLiveData: LiveData<Set<Order.Status>> = _orderFilterLiveData

	var orderFilter: Set<Order.Status>
		get() = _orderFilterLiveData.value ?: emptySet()
		set(value) = _orderFilterLiveData.postValue(value)

	fun refreshOrders() = ordersResource.reload()

	fun updateOrderStatus(order: Order, status: Order.Status)
	{
		fun updateOrderLocally(order: Order, status: Order.Status) =
				coroutineScope.launch { orderDao.updateStatus(order.id, status) }

		orderApi.updateOrderStatus(order.id, status).handleResponse { updateOrderLocally(order, status) }
	}

	fun removeAllOrders()
	{
		fun removeOrdersLocally() = coroutineScope.launch { orderDao.deleteAll() }

		orderApi.removeAllOrders().handleResponse { removeOrdersLocally() }
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_updateErrorEventLiveData,
			               successListener = successListener,
			               failureListener = { _updateErrorEventLiveData.value = Event(Unit) })
}
