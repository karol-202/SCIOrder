package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.viewmodel.UserOrdersViewModel

class UserOrdersAndroidViewModel(userAuthRepository: UserAuthRepository,
                                 orderRepository: OrderRepository) :
		UserOrdersViewModel(userAuthRepository, orderRepository)
{
	val ordersLiveData = ordersFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val errorEventLiveData = errorEventFlow.asLiveData(coroutineScope)
}
