package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.admin.AdminRepository
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.viewmodel.AdminLoginViewModel

class AdminLoginAndroidViewModel(adminRepository: AdminRepository,
                                 adminAuthRepository: AdminAuthRepository,
                                 storeRepository: StoreRepository,
                                 orderRepository: OrderRepository,
                                 productRepository: ProductRepository) :
		AdminLoginViewModel(adminRepository, adminAuthRepository, storeRepository, orderRepository, productRepository)
{
	val adminLiveData = adminFlow.asLiveData(coroutineScope)
}
