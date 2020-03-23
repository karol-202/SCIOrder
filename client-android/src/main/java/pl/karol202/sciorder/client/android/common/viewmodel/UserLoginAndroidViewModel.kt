package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.product.parameter.ProductParameterRepository
import pl.karol202.sciorder.client.common.repository.user.UserRepository
import pl.karol202.sciorder.client.common.viewmodel.UserLoginViewModel

class UserLoginAndroidViewModel(userRepository: UserRepository,
                                userAuthRepository: UserAuthRepository,
                                orderRepository: OrderRepository,
                                productRepository: ProductRepository,
                                productParameterRepository: ProductParameterRepository) :
		UserLoginViewModel(userRepository, userAuthRepository, orderRepository, productRepository, productParameterRepository)
{
	val storeLiveData = storeFlow.asLiveData(coroutineScope)
}
