package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.flowOf
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.model.Product

abstract class ProductsViewModel(ownerRepository: OwnerRepository,
                                 private val productRepository: ProductRepository) : ViewModel()
{
	protected var owner: Owner? = null
	private var productsResource: Resource<List<Product>>? = null
		set(value)
		{
			field?.close()
			field = value
		}
	
	private val productsStateBroadcastChannel = ownerRepository.getOwnerFlow()
															   .onEach { owner = it }
															   .map { it?.let { productRepository.getProductsResource(it.id) } }
															   .onEach { it?.autoReloadIn(coroutineScope) }
															   .onEach { productsResource = it }
															   .flatMapLatest { it?.asFlow ?: flowOf(null) }
															   .conflate()
															   .broadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)
	
	private val productsStateFlow = productsStateBroadcastChannel.asFlow()

	protected val productsFlow = productsStateFlow.map { it?.data.orEmpty() }
												  .distinctUntilChanged()
	protected val loadingFlow = productsStateFlow.map { it is Resource.State.Loading }
												 .distinctUntilChanged()
	protected val loadingErrorEventFlow = productsStateFlow.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
														   .distinctUntilChanged()

	fun refreshProducts() = launch { productsResource?.reload() }
	
	override fun onCleared()
	{
		super.onCleared()
		productsResource?.close()
		productsStateBroadcastChannel.cancel()
	}
}
