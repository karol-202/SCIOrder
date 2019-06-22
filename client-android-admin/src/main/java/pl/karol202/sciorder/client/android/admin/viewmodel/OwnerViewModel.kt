package pl.karol202.sciorder.client.android.admin.viewmodel

import pl.karol202.sciorder.client.android.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository

class OwnerViewModel(ownerRepository: OwnerRepository,
                     private val orderDao: OrderDao,
                     private val productDao: ProductDao) : OwnerViewModel(ownerRepository)
{
	override fun onLogout() = launch {
		orderDao.deleteAll()
		productDao.deleteAll()
	}
}
