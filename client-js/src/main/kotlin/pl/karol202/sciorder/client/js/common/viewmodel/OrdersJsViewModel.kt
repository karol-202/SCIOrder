package pl.karol202.sciorder.client.js.common.viewmodel

import kotlinx.coroutines.flow.asFlow
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.js.common.util.shareIn

class OrdersJsViewModel(ownerRepository: OwnerRepository,
                        orderRepository: OrderRepository) : OrdersViewModel(ownerRepository, orderRepository)
{
	val ordersObservable = ordersFlow.shareIn(coroutineScope)
	val loadingObservable = loadingFlow.shareIn(coroutineScope)
	val loadingErrorEventObservable = loadingErrorEventFlow.shareIn(coroutineScope)

	val updateErrorEventObservable = updateErrorEventBroadcastChannel.asFlow()

	val orderFilterObservable = orderFilterBroadcastChannel.asFlow()
}
