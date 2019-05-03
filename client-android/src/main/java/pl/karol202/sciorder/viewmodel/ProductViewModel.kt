package pl.karol202.sciorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import pl.karol202.sciorder.components.Event
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.model.local.product.ProductDao
import pl.karol202.sciorder.model.remote.product.ProductApi
import pl.karol202.sciorder.repository.ResourceState
import pl.karol202.sciorder.repository.product.ProductRepositoryImpl

class ProductViewModel(productDao: ProductDao,
                       productApi: ProductApi) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val productsRepository = ProductRepositoryImpl(coroutineScope, productDao, productApi)
	private val productsResource = productsRepository.getAllProducts()

	val productsLiveData: LiveData<List<Product>> = Transformations.map(productsResource.asLiveData) { it.data }

	val loadingLiveData: LiveData<Boolean> = Transformations.map(productsResource.asLiveData) { it is ResourceState.Loading }

	val errorEventLiveData: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>().apply {
		addSource(productsResource.asLiveData) { resourceState ->
			if(resourceState is ResourceState.Failure) value = Event(Unit)
		}
	}

	fun refreshProducts() = productsResource.reload()
}
