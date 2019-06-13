package pl.karol202.sciorder.client.android.admin.viewmodel

import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.common.model.remote.OwnerApi

class OwnerViewModelAdmin(ownerDao: OwnerDao,
                          ownerApi: OwnerApi,
                          private val orderDao: OrderDao,
                          private val productDao: ProductDao) : OwnerViewModel(ownerDao, ownerApi)
{
	override fun onLogout()
	{
		launch {
			orderDao.deleteAll()
			productDao.deleteAll()
		}
	}
}
