package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OrderComposeViewModel
import pl.karol202.sciorder.client.js.common.util.asObservable

class OrderComposeJsViewModel(ownerRepository: OwnerRepository,
                              orderTrackRepository: OrderTrackRepository) :
		OrderComposeViewModel(ownerRepository, orderTrackRepository)
{
	val orderObservable = orderBroadcastChannel.asObservable()

	val errorEventObservable = errorEventBroadcastChannel.asObservable()
}
