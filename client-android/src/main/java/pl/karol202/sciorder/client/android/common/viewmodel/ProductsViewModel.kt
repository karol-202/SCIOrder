package pl.karol202.sciorder.client.android.common.viewmodel

import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.android.common.util.Event
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
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

	val productsLiveData = productsResourceAsBroadcastChannel.asFlow().map { it.data }.asLiveData()
	val loadingLiveData = productsResourceAsBroadcastChannel.asFlow().map { it is Resource.State.Loading }.asLiveData()
	val loadingErrorEventLiveData = productsResourceAsBroadcastChannel.asFlow()
																	  .mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
																	  .asLiveData()

	fun refreshProducts() = launch { productsResource?.reload() }

	override fun onCleared()
	{
		super.onCleared()
		productsResource?.close()
		productsResourceAsBroadcastChannel.cancel()
	}
}
