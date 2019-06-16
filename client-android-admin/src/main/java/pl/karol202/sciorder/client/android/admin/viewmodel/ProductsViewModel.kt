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
import pl.karol202.sciorder.common.Product

class ProductsViewModel(ownerRepository: OwnerRepository,
                        private val productRepository: ProductRepository) : CoroutineViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}

	private val ownerFlow = ownerRepository.getOwner().conflate()

	private val productsResourceFlow = ownerFlow.filterNotNull().map { productRepository.getAllProducts(it.id) }.conflate()
	private val productsResourceAsFlow = productsResourceFlow.switchMap { it.asFlow }

	val productsLiveData = productsResourceAsFlow.map { it.data }.asLiveData()
	val loadingLiveData = productsResourceAsFlow.map { it is Resource.State.Loading }.asLiveData()
	val loadingErrorEventLiveData = productsResourceAsFlow.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
														  .asLiveData()

	private val _updateEventLiveData = MediatorLiveData<Event<UpdateResult>>()
	val updateEventLiveData: LiveData<Event<UpdateResult>> = _updateEventLiveData

	fun refreshProducts() = launch { productsResourceFlow.first().reload() }

	fun addProduct(product: Product) = launch {
		val owner = ownerFlow.first() ?: return@launch
		productRepository.addProduct(owner, product).handleResponse()

	}

	fun updateProduct(product: Product) = launch {
		val owner = ownerFlow.first() ?: return@launch
		productRepository.updateProduct(owner, product).handleResponse()
	}

	fun removeProduct(product: Product) = launch {
		val owner = ownerFlow.first() ?: return@launch
		productRepository.removeProduct(owner, product).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { _updateEventLiveData.value = Event(UpdateResult.SUCCESS) },
			     onError = { _updateEventLiveData.value = Event(UpdateResult.FAILURE) })
}
