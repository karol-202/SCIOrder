package pl.karol202.sciorder.client.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.*
import pl.karol202.sciorder.client.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.ProductApi
import pl.karol202.sciorder.client.common.repository.product.ProductRepositoryImpl
import pl.karol202.sciorder.client.common.repository.resource.EmptyResource
import pl.karol202.sciorder.client.common.repository.resource.ResourceState
import pl.karol202.sciorder.common.model.Product

class ProductsViewModel(ownerDao: OwnerDao,
                        private val productDao: ProductDao,
                        private val productApi: ProductApi) : ViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}

	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)
	private val productsRepository = ProductRepositoryImpl(coroutineScope, productDao, productApi)

	private val ownerLiveData = ownerDao.get()
	private val owner get() = ownerLiveData.value

	private val productsResourceLiveData = ownerLiveData.nonNull().map { productsRepository.getAllProducts(it.id) }
	private val productsResourceAsLiveData = productsResourceLiveData.switchMap { it.asLiveData }
	private val productsResource get() = productsResourceLiveData.value ?: EmptyResource<List<Product>>()

	val productsLiveData = productsResourceAsLiveData.map { it.data }
	val loadingLiveData = productsResourceAsLiveData.map { it is ResourceState.Loading }
	val loadingErrorEventLiveData = productsResourceAsLiveData.mapNotNull { if(it is ResourceState.Failure) Event(Unit) else null }

	private val _updateEventLiveData = MediatorLiveData<Event<UpdateResult>>()
	val updateEventLiveData: LiveData<Event<UpdateResult>> = _updateEventLiveData

	fun refreshProducts() = productsResource.reload()

	fun addProduct(product: Product)
	{
		fun addProductLocally(product: Product) = coroutineScope.launch { productDao.insert(listOf(product)) }

		val owner = owner ?: return
		productApi.addProduct(owner.id, owner.hash, product).handleResponse { addProductLocally(it) }
	}

	fun updateProduct(product: Product)
	{
		fun updateProductLocally(product: Product) = coroutineScope.launch { productDao.update(listOf(product)) }

		val owner = owner ?: return
		productApi.updateProduct(owner.id, owner.hash, product.id, product).handleResponse { updateProductLocally(product) }
	}

	fun removeProduct(product: Product)
	{
		fun removeProductLocally(product: Product) = coroutineScope.launch { productDao.delete(listOf(product)) }

		val owner = owner ?: return
		productApi.removeProduct(owner.id, owner.hash, product.id).handleResponse { removeProductLocally(product) }
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_updateEventLiveData,
			               successListener = {
				               _updateEventLiveData.value = Event(UpdateResult.SUCCESS)
				               successListener(it)
			               },
			               failureListener = { _updateEventLiveData.value = Event(UpdateResult.FAILURE) })
}
