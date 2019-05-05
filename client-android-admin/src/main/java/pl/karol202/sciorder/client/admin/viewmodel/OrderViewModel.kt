package pl.karol202.sciorder.client.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.admin.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.order.OrderApi
import pl.karol202.sciorder.client.common.repository.ResourceState
import pl.karol202.sciorder.common.model.Order

class OrderViewModel(private val orderDao: OrderDao,
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

	fun refreshOrders() = ordersResource.reload()

	fun updateOrderStatus(order: Order, status: Order.Status)
	{
		val liveData = orderApi.updateOrderStatus(order._id, status)
		handleOrderUpdateResponse(order, status, liveData)
	}

	private fun handleOrderUpdateResponse(order: Order, status: Order.Status, liveData: LiveData<ApiResponse<Unit>>)
	{
		_updateErrorEventLiveData.addSource(liveData) { apiResponse ->
			_updateErrorEventLiveData.removeSource(liveData)
			if(apiResponse is ApiResponse.Success) updateOrderLocally(order, status)
			else _updateErrorEventLiveData.value = Event(Unit)
		}
	}

	private fun updateOrderLocally(order: Order, status: Order.Status)
	{
		coroutineScope.launch { orderDao.updateOrderStatus(order._id, status) }
	}
}
