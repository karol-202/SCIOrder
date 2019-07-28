package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.util.shareIn
import pl.karol202.sciorder.client.common.viewmodel.OrdersViewModel
import pl.karol202.sciorder.common.Order

class OrdersJsViewModel(ownerRepository: OwnerRepository,
                        orderRepository: OrderRepository) : OrdersViewModel(ownerRepository, orderRepository)
{
	val filterObservable = filterFlow.shareIn(coroutineScope)
	val anyOrdersPresentObservable = anyOrdersPresentFlow.shareIn(coroutineScope)
	val ordersObservable = ordersFlow.shareIn(coroutineScope)
	//val loadingObservable = loadingFlow.shareIn(coroutineScope)
	val loadingErrorEventObservable = loadingErrorEventFlow.shareIn(coroutineScope)
	val updateErrorEventObservable = updateErrorEventFlow.shareIn(coroutineScope)
	
	fun toggleOrderFilter(status: Order.Status)
	{
		if(status in orderFilter) orderFilter -= status
		else orderFilter += status
	}
}
