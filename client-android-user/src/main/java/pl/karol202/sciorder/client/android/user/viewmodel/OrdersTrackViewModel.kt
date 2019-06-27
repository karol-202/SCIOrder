package pl.karol202.sciorder.client.android.user.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OrdersTrackViewModel

class OrdersTrackViewModel(ownerRepository: OwnerRepository,
                           orderRepository: OrderTrackRepository) : OrdersTrackViewModel(ownerRepository, orderRepository)
{
	val ordersLiveData = ordersFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val errorEventLiveData = errorEventFlow.asLiveData(coroutineScope)
}
