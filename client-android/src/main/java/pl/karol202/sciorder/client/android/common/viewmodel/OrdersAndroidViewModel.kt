package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.AdminOrdersViewModel

class OrdersAndroidViewModel(ownerRepository: OwnerRepository,
                             orderRepository: OrderRepository) : AdminOrdersViewModel(ownerRepository, orderRepository)
{
	val ordersLiveData = ordersFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val loadingErrorEventLiveData = loadingErrorEventFlow.asLiveData(coroutineScope)
	val updateErrorEventLiveData = updateErrorEventFlow.asLiveData(coroutineScope)
}
