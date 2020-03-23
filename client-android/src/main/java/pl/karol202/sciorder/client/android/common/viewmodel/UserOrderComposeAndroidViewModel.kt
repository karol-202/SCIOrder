package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.viewmodel.UserOrderComposeViewModel

class UserOrderComposeAndroidViewModel(userAuthRepository: UserAuthRepository,
                                       orderRepository: OrderRepository) :
		UserOrderComposeViewModel(userAuthRepository, orderRepository)
{
	val orderLiveData = orderFlow.asLiveData(coroutineScope)
	val errorEventLiveData = errorEventFlow.asLiveData(coroutineScope)
}
