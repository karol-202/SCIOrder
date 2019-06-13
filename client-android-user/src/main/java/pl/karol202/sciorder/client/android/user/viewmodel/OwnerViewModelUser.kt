package pl.karol202.sciorder.client.android.user.viewmodel

import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.common.model.remote.OwnerApi

class OwnerViewModelUser(ownerDao: OwnerDao,
                         ownerApi: OwnerApi,
                         private val productDao: ProductDao) : OwnerViewModel(ownerDao, ownerApi)
{
	override fun onLogout()
	{
		launch { productDao.deleteAll() }
	}
}
