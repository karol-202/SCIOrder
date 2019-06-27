package pl.karol202.sciorder.client.android.common.viewmodel

import pl.karol202.sciorder.client.android.common.util.asLiveData
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.viewmodel.ProductsViewModel

abstract class ProductsViewModel(ownerRepository: OwnerRepository,
                                 productRepository: ProductRepository) : ProductsViewModel(ownerRepository, productRepository)
{
	val productsLiveData = productsFlow.asLiveData(coroutineScope)
	val loadingLiveData = loadingFlow.asLiveData(coroutineScope)
	val loadingErrorEventLiveData = loadingErrorEventFlow.asLiveData(coroutineScope)
}
