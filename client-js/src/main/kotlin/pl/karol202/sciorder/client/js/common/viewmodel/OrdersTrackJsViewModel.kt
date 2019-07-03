package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OrdersTrackViewModel
import pl.karol202.sciorder.client.js.common.util.asObservable

class OrdersTrackJsViewModel(ownerRepository: OwnerRepository,
                             orderRepository: OrderTrackRepository) : OrdersTrackViewModel(ownerRepository, orderRepository)
{
	val ordersObservable = ordersFlow.asObservable()
	val loadingObservable = loadingFlow.asObservable()
	val errorEventObservable = errorEventFlow.asObservable()
}
