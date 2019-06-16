package pl.karol202.sciorder.client.android.user.viewmodel

import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

class OwnerViewModelUser(ownerRepository: OwnerRepository,
                         private val productDao: ProductDao) : OwnerViewModel(ownerRepository)
{
	override fun onLogout() = launch { productDao.deleteAll() }
}
