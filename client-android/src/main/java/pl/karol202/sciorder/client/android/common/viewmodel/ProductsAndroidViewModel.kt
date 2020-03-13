package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.viewmodel.UserProductsViewModel

class ProductsAndroidViewModel(ownerRepository: OwnerRepository,
                                    productRepository: ProductRepository) : UserProductsViewModel(ownerRepository, productRepository)
{
	val productsLiveData = productsFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val loadingErrorEventLiveData = loadingErrorEventFlow.asLiveData(coroutineScope)
}
