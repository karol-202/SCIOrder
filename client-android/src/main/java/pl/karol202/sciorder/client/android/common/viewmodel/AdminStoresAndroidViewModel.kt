package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.viewmodel.AdminStoresViewModel

class AdminStoresAndroidViewModel(adminAuthRepository: AdminAuthRepository,
                                  storeRepository: StoreRepository) :
		AdminStoresViewModel(adminAuthRepository, storeRepository)
{
	val storesLiveData = storesFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val loadingErrorEventLiveData = loadingErrorEventFlow.asLiveData(coroutineScope)
	val updateEventLiveData = updateEventFlow.asLiveData(coroutineScope)
	val selectedStoreLiveData = selectedStoreFlow.asLiveData(coroutineScope)
}
