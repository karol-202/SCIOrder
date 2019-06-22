package pl.karol202.sciorder.client.android.user.viewmodel

import kotlinx.coroutines.flow.asFlow
import pl.karol202.sciorder.client.android.common.extension.asLiveData
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OrderComposeViewModel

class OrderComposeViewModel(ownerRepository: OwnerRepository,
                            orderTrackRepository: OrderTrackRepository) : OrderComposeViewModel(ownerRepository, orderTrackRepository)
{
	val orderLiveData = orderBroadcastChannel.asFlow().asLiveData(coroutineScope)

	val errorEventLiveData = errorEventBroadcastChannel.asFlow().asLiveData(coroutineScope)
}
