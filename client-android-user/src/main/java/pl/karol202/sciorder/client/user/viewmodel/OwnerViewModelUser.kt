package pl.karol202.sciorder.client.user.viewmodel

import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

class OwnerViewModelUser(ownerDao: OwnerDao,
                         ownerApi: OwnerApi,
                         private val productDao: ProductDao) : OwnerViewModel(ownerDao, ownerApi)
{
	override fun onLogout()
	{
		launch { productDao.deleteAll() }
	}
}
