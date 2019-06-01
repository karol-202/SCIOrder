package pl.karol202.sciorder.client.android.admin.viewmodel

import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.android.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.android.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.android.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerViewModel

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
