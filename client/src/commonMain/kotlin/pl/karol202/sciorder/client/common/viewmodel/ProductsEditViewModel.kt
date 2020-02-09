package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.model.Product

abstract class ProductsEditViewModel(ownerRepository: OwnerRepository,
                                     private val productRepository: ProductRepository) :
		ProductsViewModel(ownerRepository, productRepository)
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}
	
	private val updateEventBroadcastChannel = ConflatedBroadcastChannel<Event<UpdateResult>>()

	protected val updateEventFlow = updateEventBroadcastChannel.asFlow()
															   .distinctUntilChanged()

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
		updateEventBroadcastChannel.cancel()
	}
}
