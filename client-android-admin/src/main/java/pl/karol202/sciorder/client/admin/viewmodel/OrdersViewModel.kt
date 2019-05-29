package pl.karol202.sciorder.client.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.admin.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.*
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.common.repository.resource.ResourceState
import pl.karol202.sciorder.client.common.settings.Settings
import pl.karol202.sciorder.client.common.settings.liveString
import pl.karol202.sciorder.common.model.Order

class OrdersViewModel(private val orderDao: OrderDao,
                      private val orderApi: OrderApi,
                      settings: Settings) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)
	private val ordersRepository = OrderRepositoryImpl(coroutineScope, orderDao, orderApi)

	private val _ownerIdSettingLiveData = settings.liveString("ownerId", null)
	private val _hashSettingLiveData = settings.liveString("hash", null)
	private val ownerId get() = _ownerIdSettingLiveData.value
	private val hash get() = _hashSettingLiveData.value

	private val ordersResourceLiveData = (_ownerIdSettingLiveData.nonNull() + _hashSettingLiveData.nonNull()).map {
		(ownerId, hash) -> ordersRepository.getAllOrders(ownerId, hash)
	}
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

	fun updateOrderStatus(order: Order, status: Order.Status)
	{
		fun updateOrderLocally(order: Order, status: Order.Status) =
				coroutineScope.launch { orderDao.updateStatus(order.id, status) }

		val ownerId = ownerId ?: return
		val hash = hash ?: return
		orderApi.updateOrderStatus(ownerId, order.id, hash, status).handleResponse { updateOrderLocally(order, status) }
	}

	fun removeAllOrders()
	{
		fun removeOrdersLocally() = coroutineScope.launch { orderDao.deleteAll() }

		val ownerId = ownerId ?: return
		val hash = hash ?: return
		orderApi.removeAllOrders(ownerId, hash).handleResponse { removeOrdersLocally() }
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_updateErrorEventLiveData,
			               successListener = successListener,
			               failureListener = { _updateErrorEventLiveData.value = Event(Unit) })
}
