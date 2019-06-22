package pl.karol202.sciorder.client.android.common.viewmodel

import kotlinx.coroutines.flow.asFlow
import pl.karol202.sciorder.client.android.common.extension.asLiveData
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

abstract class OwnerViewModel(ownerRepository: OwnerRepository) : OwnerViewModel(ownerRepository)
{
	val ownerLiveData = ownerFlow.asLiveData(coroutineScope)
	val errorEventLiveData = errorEventBroadcastChannel.asFlow().asLiveData(coroutineScope)
}