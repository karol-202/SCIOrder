package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.util.shareIn
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

abstract class OwnerJsViewModel(ownerRepository: OwnerRepository) : OwnerViewModel(ownerRepository)
{
	val ownerObservable = ownerFlow.shareIn(coroutineScope)
	val errorEventObservable = errorEventFlow.shareIn(coroutineScope)
}

class AdminOwnerJsViewModel(ownerRepository: OwnerRepository,
                            private val orderDao: OrderDao,
                            private val productDao: ProductDao) : OwnerJsViewModel(ownerRepository)
{
	override fun onLogout() = launch {
		orderDao.deleteAll()
		productDao.deleteAll()
	}
}

class UserOwnerJsViewModel(ownerRepository: OwnerRepository,
                           private val productDao: ProductDao) : OwnerJsViewModel(ownerRepository)
{
	override fun onLogout() = launch {
		productDao.deleteAll()
	}
}
