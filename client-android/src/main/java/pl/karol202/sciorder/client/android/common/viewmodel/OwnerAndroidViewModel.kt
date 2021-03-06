package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

abstract class OwnerAndroidViewModel(ownerRepository: OwnerRepository) : OwnerViewModel(ownerRepository)
{
	val ownerLiveData = ownerFlow.asLiveData(coroutineScope)
	val errorEventLiveData = errorEventFlow.asLiveData(coroutineScope)
}
