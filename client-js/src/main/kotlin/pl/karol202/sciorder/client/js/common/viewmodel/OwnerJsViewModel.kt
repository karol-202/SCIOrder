package pl.karol202.sciorder.client.js.common.viewmodel

import kotlinx.coroutines.flow.asFlow
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.js.common.util.shareIn

class OwnerJsViewModel(ownerRepository: OwnerRepository,
                       private val orderDao: OrderDao,
                       private val productDao: ProductDao) : OwnerViewModel(ownerRepository)
{
	val ownerObservable = ownerFlow.shareIn(coroutineScope)
	val errorEventObservable = errorEventBroadcastChannel.asFlow()

	override fun onLogout() = launch {
		orderDao.deleteAll()
		productDao.deleteAll()
	}
}
