package pl.karol202.sciorder.client.js.common.viewmodel

import kotlinx.coroutines.flow.asFlow
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.js.common.util.shareIn

class ProductJsViewModel(ownerRepository: OwnerRepository,
                         productRepository: ProductRepository) : ProductsViewModel(ownerRepository, productRepository)
{
	val productsObservable = productsFlow.shareIn(coroutineScope)
	val loadingObservable = loadingFlow.shareIn(coroutineScope)
	val loadingErrorEventObservable = loadingErrorEventFlow.shareIn(coroutineScope)

	val updateErrorEventObservable = updateEventBroadcastChannel.asFlow()
}
