package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OrdersViewModel
import pl.karol202.sciorder.client.js.common.util.asObservable

class OrdersJsViewModel(ownerRepository: OwnerRepository,
                        orderRepository: OrderRepository) : OrdersViewModel(ownerRepository, orderRepository)
{
	val ordersObservable = ordersFlow.asObservable()
	val loadingObservable = loadingFlow.asObservable()
	val loadingErrorEventObservable = loadingErrorEventFlow.asObservable()

	val updateErrorEventObservable = updateErrorEventBroadcastChannel.asObservable()

	val orderFilterObservable = orderFilterBroadcastChannel.asObservable()
}
