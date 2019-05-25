package pl.karol202.sciorder.client.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.handleResponse
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.client.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.product.ProductApi
import pl.karol202.sciorder.client.common.repository.ResourceState
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl

class ProductsViewModel(private val productDao: ProductDao,
                        private val productApi: ProductApi) : ViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}

	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val productsRepository = ProductRepositoryImpl(coroutineScope, productDao, productApi)
	private val productsResource = productsRepository.getAllProducts()

	val productsLiveData: LiveData<List<Product>> = Transformations.map(productsResource.asLiveData) { it.data }

	val loadingLiveData: LiveData<Boolean> = Transformations.map(productsResource.asLiveData) { it is ResourceState.Loading }

	val loadingErrorEventLiveData: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>().apply {
		addSource(productsResource.asLiveData) { resourceState ->
			if(resourceState is ResourceState.Failure) value = Event(Unit)
		}
	}

	private val _updateEventLiveData = MediatorLiveData<Event<UpdateResult>>()
	val updateEventLiveData: LiveData<Event<UpdateResult>> = _updateEventLiveData

	fun refreshProducts() = productsResource.reload()

	fun addProduct(product: Product)
	{
		fun addProductLocally(product: Product) = coroutineScope.launch { productDao.insert(listOf(product)) }

		productApi.addProduct(product).handleResponse { addProductLocally(it) }
	}

	fun updateProduct(product: Product)
	{
		fun updateProductLocally(product: Product) = coroutineScope.launch { productDao.update(listOf(product)) }

		productApi.updateProduct(product).handleResponse { updateProductLocally(product) }
	}

	fun removeProduct(product: Product)
	{
		fun removeProductLocally(product: Product) = coroutineScope.launch { productDao.delete(listOf(product)) }

		productApi.removeProduct(product.id).handleResponse { removeProductLocally(product) }
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_updateEventLiveData,
			               successListener = {
				               _updateEventLiveData.value = Event(UpdateResult.SUCCESS)
				               successListener(it)
			               },
			               failureListener = { _updateEventLiveData.value = Event(UpdateResult.FAILURE) })
}
