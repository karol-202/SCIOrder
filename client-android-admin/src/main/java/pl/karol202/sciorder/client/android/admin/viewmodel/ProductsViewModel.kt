package pl.karol202.sciorder.client.android.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.client.android.common.components.CoroutineViewModel
import pl.karol202.sciorder.client.android.common.components.Event
import pl.karol202.sciorder.client.android.common.extensions.*
import pl.karol202.sciorder.client.android.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.android.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.android.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.android.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.android.common.model.remote.ProductApi
import pl.karol202.sciorder.client.android.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.android.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.android.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.android.common.repository.resource.ResourceState
import pl.karol202.sciorder.common.model.Product

class ProductsViewModel(ownerDao: OwnerDao,
                        ownerApi: OwnerApi,
                        productDao: ProductDao,
                        productApi: ProductApi) : CoroutineViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}

	private val ownerRepository = OwnerRepositoryImpl(coroutineScope, ownerDao, ownerApi)
	private val productRepository = ProductRepositoryImpl(coroutineScope, productDao, productApi)

	private val ownerLiveData = ownerRepository.getOwner()
	private val owner get() = ownerLiveData.value

	private val productsResourceLiveData = ownerLiveData.nonNull().map { productRepository.getAllProducts(it.id) }
	private val productsResourceAsLiveData = productsResourceLiveData.switchMap { it.asLiveData }
	private val productsResource get() = productsResourceLiveData.value ?: EmptyResource<List<Product>>()

	val productsLiveData = productsResourceAsLiveData.map { it.data }
	val loadingLiveData = productsResourceAsLiveData.map { it is ResourceState.Loading }
	val loadingErrorEventLiveData = productsResourceAsLiveData.mapNotNull { if(it is ResourceState.Failure) Event(Unit) else null }

	private val _updateEventLiveData = MediatorLiveData<Event<UpdateResult>>()
	val updateEventLiveData: LiveData<Event<UpdateResult>> = _updateEventLiveData

	fun refreshProducts() = productsResource.reload()

	fun addProduct(product: Product) = owner?.let { owner ->
		productRepository.addProduct(owner, product).handleResponse()
	}

	fun updateProduct(product: Product) = owner?.let { owner ->
		productRepository.updateProduct(owner, product).handleResponse()
	}

	fun removeProduct(product: Product) = owner?.let { owner ->
		productRepository.removeProduct(owner, product).handleResponse()
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse() =
			handleResponse(successListener = { _updateEventLiveData.value = Event(UpdateResult.SUCCESS) },
			               failureListener = { _updateEventLiveData.value = Event(UpdateResult.FAILURE) })
}
