package pl.karol202.sciorder.client.js.common.viewmodel

import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.js.common.util.asObservable

class ProductJsViewModel(ownerRepository: OwnerRepository,
                         productRepository: ProductRepository) : ProductsViewModel(ownerRepository, productRepository)
{
	val productsObservable = productsFlow.asObservable()
	val loadingObservable = loadingFlow.asObservable()
	val loadingErrorEventObservable = loadingErrorEventFlow.asObservable()

	val updateErrorEventObservable = updateEventBroadcastChannel.asObservable()
}
