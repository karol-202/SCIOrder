package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.util.shareIn
import pl.karol202.sciorder.client.common.viewmodel.OrdersTrackViewModel

class OrdersTrackJsViewModel(ownerRepository: OwnerRepository,
                             orderRepository: OrderTrackRepository) : OrdersTrackViewModel(ownerRepository, orderRepository)
{
	val ordersObservable = ordersFlow.shareIn(coroutineScope)
	val loadingObservable = loadingFlow.shareIn(coroutineScope)
	val errorEventObservable = errorEventFlow.shareIn(coroutineScope)
}
