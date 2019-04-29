package pl.karol202.sciorder.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import pl.karol202.sciorder.components.Event
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.repository.ProductRepository
import pl.karol202.sciorder.repository.ResourceState

class ProductViewModel(application: Application) : AndroidViewModel(application)
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val productsRepository = ProductRepository.create(coroutineScope, application)
	private val productsResource = productsRepository.getAllProducts()

	val productsLiveData: LiveData<List<Product>> = Transformations.map(productsResource.asLiveData) { it.data }

	private val _loadingLiveData = MediatorLiveData<Boolean>().apply {
		addSource(productsResource.asLiveData) { resourceState ->
			value = resourceState is ResourceState.Loading
		}
	}
	val loadingLiveData: LiveData<Boolean> = _loadingLiveData

	private val _errorEventLiveData = MediatorLiveData<Event<Unit>>().apply {
		addSource(productsResource.asLiveData) { resourceState ->
			if(resourceState is ResourceState.Failure) value = Event(Unit)
		}
	}
	val errorEventLiveData: LiveData<Event<Unit>> = _errorEventLiveData

	fun refreshProducts() = productsResource.reload()
}