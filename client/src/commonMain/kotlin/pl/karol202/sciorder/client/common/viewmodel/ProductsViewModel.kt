package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.Owner
import pl.karol202.sciorder.common.Product

abstract class ProductsViewModel(ownerRepository: OwnerRepository,
                                 productRepository: ProductRepository) : CoroutineViewModel()
{
	protected var owner: Owner? = null
	private var productsResource: Resource<List<Product>>? = null
		set(value)
		{
			field?.close()
			field = value
		}

	private val productsResourceAsBroadcastChannel = ownerRepository.getOwnerFlow()
																	.onEach { owner = it }
																	.filterNotNull()
																	.map { productRepository.getProductsResource(it.id) }
																	.onEach { it.autoReloadIn(coroutineScope) }
																	.onEach { productsResource = it }
																	.switchMap { it.asFlow }
																	.conflate()
																	.broadcastIn(coroutineScope)

	protected val productsFlow = productsResourceAsBroadcastChannel.asFlow().map { it.data }
	protected val loadingFlow = productsResourceAsBroadcastChannel.asFlow().map { it is Resource.State.Loading }
	protected val loadingErrorEventFlow = productsResourceAsBroadcastChannel.asFlow()
																	        .mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }

	fun refreshProducts() = launch { productsResource?.reload() }

	override fun onCleared()
	{
		super.onCleared()
		productsResource?.close()
		productsResourceAsBroadcastChannel.cancel()
	}
}
