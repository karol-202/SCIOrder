package pl.karol202.sciorder.client.android.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.client.android.common.viewmodel.ProductsViewModel
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.Product

class ProductsViewModel(ownerRepository: OwnerRepository,
                        private val productRepository: ProductRepository) : ProductsViewModel(ownerRepository, productRepository)
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}

	private val _updateEventLiveData = MediatorLiveData<Event<UpdateResult>>()
	val updateEventLiveData: LiveData<Event<UpdateResult>> = _updateEventLiveData

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
}
