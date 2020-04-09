package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.viewmodel.AdminOrdersViewModel

class AdminOrdersAndroidViewModel(adminAuthRepository: AdminAuthRepository,
                                  storeRepository: StoreRepository,
                                  orderRepository: OrderRepository,
                                  productRepository: ProductRepository) :
		AdminOrdersViewModel(adminAuthRepository, storeRepository, orderRepository, productRepository)
{
	val ordersLiveData = ordersFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val loadingErrorEventLiveData = loadingErrorEventFlow.asLiveData(coroutineScope)
	val updateErrorEventLiveData = updateErrorEventFlow.asLiveData(coroutineScope)
}
