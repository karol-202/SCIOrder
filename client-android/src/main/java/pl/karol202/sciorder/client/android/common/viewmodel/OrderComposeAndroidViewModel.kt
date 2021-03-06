package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OrderComposeViewModel

class OrderComposeAndroidViewModel(ownerRepository: OwnerRepository,
                                   orderTrackRepository: OrderTrackRepository) : OrderComposeViewModel(ownerRepository, orderTrackRepository)
{
	val orderLiveData = orderFlow.asLiveData(coroutineScope)
	val errorEventLiveData = errorEventFlow.asLiveData(coroutineScope)
}
