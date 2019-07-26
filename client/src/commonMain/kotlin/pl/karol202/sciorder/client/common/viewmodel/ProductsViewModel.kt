package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.Owner
import pl.karol202.sciorder.common.Product

abstract class ProductsViewModel(ownerRepository: OwnerRepository,
                                 private val productRepository: ProductRepository) : CoroutineViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}

	protected var owner: Owner? = null
	private var productsResource: Resource<List<Product>>? = null
		set(value)
		{
			field?.close()
			field = value
		}
	
	private val productsStateBroadcastChannel = ownerRepository.getOwnerFlow()
															   .onEach { owner = it }
															   .filterNotNull()
															   .map { productRepository.getProductsResource(it.id) }
															   .onEach { it.autoReloadIn(coroutineScope) }
															   .onEach { productsResource = it }
															   .switchMap { it.asFlow }
															   .conflate()
															   .broadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)
	private val updateEventBroadcastChannel = ConflatedBroadcastChannel<Event<UpdateResult>>()
	
	private val productsStateFlow = productsStateBroadcastChannel.asFlow()

	protected val productsFlow = productsStateFlow.map { it.data }
												  .distinctUntilChanged()
	protected val loadingFlow = productsStateFlow.map { it is Resource.State.Loading }
												 .distinctUntilChanged()
	protected val loadingErrorEventFlow = productsStateFlow.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
														   .distinctUntilChanged()
	protected val updateEventFlow = updateEventBroadcastChannel.asFlow()
															   .distinctUntilChanged()

	fun refreshProducts() = launch { productsResource?.reload() }

	fun addProduct(product: Product) = launch {
		productRepository.addProduct(owner ?: return@launch, product).handleResponse()
	}

	fun updateProduct(product: Product) = launch {
		productRepository.updateProduct(owner ?: return@launch, product).handleResponse()
	}

	fun removeProduct(product: Product) = launch {
		productRepository.removeProduct(owner ?: return@launch, product).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { updateEventBroadcastChannel.offer(Event(UpdateResult.SUCCESS)) },
			     onError = { updateEventBroadcastChannel.offer(Event(UpdateResult.FAILURE)) })

	override fun onCleared()
	{
		super.onCleared()
		productsResource?.close()
		productsStateBroadcastChannel.cancel()
		updateEventBroadcastChannel.cancel()
	}
}
