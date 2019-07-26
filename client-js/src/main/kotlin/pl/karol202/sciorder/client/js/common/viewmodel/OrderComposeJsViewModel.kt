package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.util.shareIn
import pl.karol202.sciorder.client.common.viewmodel.OrderComposeViewModel

class OrderComposeJsViewModel(ownerRepository: OwnerRepository,
                              orderTrackRepository: OrderTrackRepository) :
		OrderComposeViewModel(ownerRepository, orderTrackRepository)
{
	val orderObservable = orderFlow.shareIn(coroutineScope)
	val errorEventObservable = errorEventFlow.shareIn(coroutineScope)
}
