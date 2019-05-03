package pl.karol202.sciorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.components.Event
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.local.order.OrderDao
import pl.karol202.sciorder.model.remote.order.OrderApi
import pl.karol202.sciorder.repository.ResourceState
import pl.karol202.sciorder.repository.order.OrderRepositoryImpl

class OrderTrackViewModel(private val orderDao: OrderDao,
                          orderApi: OrderApi) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val ordersRepository = OrderRepositoryImpl(coroutineScope, orderDao, orderApi)
	private val ordersResource = ordersRepository.getTrackedOrders()

	val ordersLiveData: LiveData<List<Order>> = Transformations.map(ordersResource.asLiveData) { it.data }

	val loadingLiveData: LiveData<Boolean> = Transformations.map(ordersResource.asLiveData) { it is ResourceState.Loading }

	val errorEventLiveData: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>().apply {
		addSource(ordersResource.asLiveData) { resourceState ->
			if(resourceState is ResourceState.Failure) value = Event(Unit)
		}
	}

	fun refreshOrders() = ordersResource.reload()

	fun removeOrder(order: Order)
	{
		coroutineScope.launch { orderDao.deleteOrder(order) }
	}
}
