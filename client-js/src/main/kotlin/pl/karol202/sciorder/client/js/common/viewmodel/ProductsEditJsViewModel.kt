package pl.karol202.sciorder.client.js.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combineLatest
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.common.model.create
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.util.shareIn
import pl.karol202.sciorder.client.common.util.uuid
import pl.karol202.sciorder.client.common.viewmodel.ProductsEditViewModel
import pl.karol202.sciorder.common.Product

class ProductsEditJsViewModel(ownerRepository: OwnerRepository,
                              productRepository: ProductRepository) : ProductsEditViewModel(ownerRepository, productRepository)
{
	private val newProductsIdsBroadcastChannel = ConflatedBroadcastChannel<List<String>>(emptyList())
	
	private val newProductsFlow = newProductsIdsBroadcastChannel.asFlow()
																.map { ids -> ids.map { Product.create(id = it) } }
	
	val productsObservable = productsFlow.combineLatest(newProductsFlow) { products, newProducts -> products + newProducts }
										 .shareIn(coroutineScope)
	//val loadingObservable = loadingFlow.shareIn(coroutineScope)
	val loadingErrorEventObservable = loadingErrorEventFlow.shareIn(coroutineScope)
	val updateEventObservable = updateEventFlow.shareIn(coroutineScope)
	
	fun newProduct() = addToNewProducts(uuid())
	
	fun applyProduct(product: Product) =
			if(product.id in newProductsIdsBroadcastChannel.value)
			{
				addProduct(product)
				removeFromNewProducts(product.id)
			}
			else updateProduct(product)
	
	private fun addToNewProducts(id: String)
	{
		newProductsIdsBroadcastChannel.offer(newProductsIdsBroadcastChannel.value + id)
	}
	
	private fun removeFromNewProducts(id: String)
	{
		newProductsIdsBroadcastChannel.offer(newProductsIdsBroadcastChannel.value - id)
	}
	
	override fun onCleared()
	{
		super.onCleared()
		newProductsIdsBroadcastChannel.cancel()
	}
}
