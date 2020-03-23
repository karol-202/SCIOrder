package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.viewmodel.UserProductsViewModel

class UserProductsAndroidViewModel(userAuthRepository: UserAuthRepository,
                                   productRepository: ProductRepository) :
		UserProductsViewModel(userAuthRepository, productRepository)
{
	val productsLiveData = productsFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val loadingErrorEventLiveData = loadingErrorEventFlow.asLiveData(coroutineScope)
}
