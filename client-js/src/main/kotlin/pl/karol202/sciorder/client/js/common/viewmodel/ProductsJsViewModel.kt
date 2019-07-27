package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.util.shareIn
import pl.karol202.sciorder.client.common.viewmodel.ProductsViewModel

class ProductsJsViewModel(ownerRepository: OwnerRepository,
                          productRepository: ProductRepository) : ProductsViewModel(ownerRepository, productRepository)
{
	val productsObservable = productsFlow.shareIn(coroutineScope)
	//val loadingObservable = loadingFlow.shareIn(coroutineScope)
	val loadingErrorEventObservable = loadingErrorEventFlow.shareIn(coroutineScope)
}
