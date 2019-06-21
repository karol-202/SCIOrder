package pl.karol202.sciorder.client.android.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.viewmodel.CoroutineViewModel
import pl.karol202.sciorder.common.Owner
import pl.karol202.sciorder.common.Product

class ProductsViewModel(ownerRepository: OwnerRepository,
                        private val productRepository: ProductRepository) : CoroutineViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}

	private var owner: Owner? = null
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

	private val _updateEventLiveData = MediatorLiveData<Event<UpdateResult>>()
	val updateEventLiveData: LiveData<Event<UpdateResult>> = _updateEventLiveData

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
			fold(onSuccess = { _updateEventLiveData.postValue(Event(UpdateResult.SUCCESS)) },
			     onError = { _updateEventLiveData.postValue(Event(UpdateResult.FAILURE)) })

	override fun onCleared()
	{
		super.onCleared()
		productsResource?.close()
		productsResourceAsBroadcastChannel.cancel()
	}
}
