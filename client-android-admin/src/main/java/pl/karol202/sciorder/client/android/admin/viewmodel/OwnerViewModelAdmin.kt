package pl.karol202.sciorder.client.android.admin.viewmodel

import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

class OwnerViewModelAdmin(ownerRepository: OwnerRepository,
                          private val orderDao: OrderDao,
                          private val productDao: ProductDao) : OwnerViewModel(ownerRepository)
{
	override fun onLogout() = launch {
		orderDao.deleteAll()
		productDao.deleteAll()
	}
}
