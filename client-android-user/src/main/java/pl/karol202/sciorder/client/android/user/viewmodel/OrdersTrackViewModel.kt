package pl.karol202.sciorder.client.android.user.viewmodel

import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.components.CoroutineViewModel
import pl.karol202.sciorder.client.android.common.components.Event
import pl.karol202.sciorder.client.android.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.android.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.android.common.repository.resource.ResourceState
import pl.karol202.sciorder.client.android.user.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.common.model.Order

class OrdersTrackViewModel(ownerDao: OwnerDao,
                           ownerApi: OwnerApi,
                           private val orderDao: OrderDao,
                           orderApi: OrderApi) : CoroutineViewModel()
{
	private val ownerRepository = OwnerRepositoryImpl(coroutineScope, ownerDao, ownerApi)
	private val orderRepository = OrderRepositoryImpl(coroutineScope, orderDao, orderApi)

	private val ownerLiveData = ownerRepository.getOwner()

	private val ordersResourceLiveData = ownerLiveData.nonNull().map { orderRepository.getTrackedOrders(it.id) }
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
