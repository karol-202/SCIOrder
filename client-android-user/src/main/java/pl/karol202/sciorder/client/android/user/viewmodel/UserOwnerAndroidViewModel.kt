package pl.karol202.sciorder.client.android.user.viewmodel

import pl.karol202.sciorder.client.android.common.viewmodel.OwnerAndroidViewModel
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository

class UserOwnerAndroidViewModel(ownerRepository: OwnerRepository,
                                private val productDao: ProductDao) : OwnerAndroidViewModel(ownerRepository)
{
	override fun onLogout() = launch { productDao.deleteAll() }
}
