package pl.karol202.sciorder.client.android.admin.viewmodel

import pl.karol202.sciorder.client.android.common.viewmodel.OwnerAndroidViewModel
import pl.karol202.sciorder.client.common.database.order.OrderDao
import pl.karol202.sciorder.client.common.database.product.ProductDao
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository

class AdminOwnerAndroidViewModel(ownerRepository: OwnerRepository,
                                 private val orderDao: OrderDao,
                                 private val productDao: ProductDao) : OwnerAndroidViewModel(ownerRepository)
{
	override fun onLogout() = launch {
		orderDao.deleteAll()
		productDao.deleteAll()
	}
}
