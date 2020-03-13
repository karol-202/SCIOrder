package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.viewmodel.AdminProductsViewModel

class ProductsEditAndroidViewModel(ownerRepository: OwnerRepository,
                                        productRepository: ProductRepository) :
		AdminProductsViewModel(ownerRepository, productRepository)
{
	val productsLiveData = productsFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val loadingErrorEventLiveData = loadingErrorEventFlow.asLiveData(coroutineScope)
	val updateEventLiveData = updateEventFlow.asLiveData(coroutineScope)
}
