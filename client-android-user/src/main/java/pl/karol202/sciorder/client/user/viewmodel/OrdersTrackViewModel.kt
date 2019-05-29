package pl.karol202.sciorder.client.user.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.map
import pl.karol202.sciorder.client.common.extensions.mapNotNull
import pl.karol202.sciorder.client.common.extensions.nonNull
import pl.karol202.sciorder.client.common.extensions.switchMap
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.common.repository.resource.ResourceState
import pl.karol202.sciorder.client.common.settings.Settings
import pl.karol202.sciorder.client.common.settings.liveString
import pl.karol202.sciorder.client.user.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.common.model.Order

class OrdersTrackViewModel(private val orderDao: OrderDao,
                           orderApi: OrderApi,
                           settings: Settings) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)
	private val ordersRepository = OrderRepositoryImpl(coroutineScope, orderDao, orderApi)

	private val _ownerIdSettingLiveData = settings.liveString("ownerId", null)

	private val ordersResourceLiveData = _ownerIdSettingLiveData.nonNull().map { ordersRepository.getTrackedOrders(it) }
	private val ordersResourceAsLiveData = ordersResourceLiveData.switchMap { it.asLiveData }
	private val ordersResource get() = ordersResourceLiveData.value ?: EmptyResource<List<Order>>()

	val ordersLiveData = ordersResourceAsLiveData.map { it.data }
	val loadingLiveData = ordersResourceAsLiveData.map { it is ResourceState.Loading }
	val errorEventLiveData = ordersResourceAsLiveData.mapNotNull { if(it is ResourceState.Failure) Event(Unit) else null }

	fun refreshOrders() = ordersResource.reload()

	fun removeOrder(order: Order)
	{
		coroutineScope.launch { orderDao.delete(listOf(order)) }
	}
}
