package pl.karol202.sciorder.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.repository.ProductRepository
import pl.karol202.sciorder.repository.ResourceState

class ProductsViewModel(application: Application) : AndroidViewModel(application)
{
	companion object
	{
		private const val ERROR_TIMEOUT_MILLIS = 2000L
	}

	sealed class State
	{
		companion object
		{
			fun fromResourceState(resourceState: ResourceState<*>) = when(resourceState)
			{
				is ResourceState.Success -> Loaded
				is ResourceState.Loading -> Loading
				is ResourceState.Failure -> Error
			}
		}

		object Loaded : State()
		object Loading : State()
		object Error : State()
	}

	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val productsRepository = ProductRepository.create(coroutineScope, application)
	private val productsResource = productsRepository.getAllProducts()

	val productsLiveData: LiveData<List<Product>> = Transformations.map(productsResource.asLiveData) { it.data }

	private val _stateLiveData = MediatorLiveData<State>().apply {
		addSource(productsResource.asLiveData) { resourceState ->
			cancelErrorDismiss()
			val state = State.fromResourceState(resourceState)
			postValue(state)
			if(state is State.Error) waitAndDismissError()
		}
	}
	val stateLiveData: LiveData<State> = _stateLiveData

	private var errorDismissJob: Job? = null

	private fun waitAndDismissError()
	{
		errorDismissJob = coroutineScope.launch {
			delay(ERROR_TIMEOUT_MILLIS)
			if(_stateLiveData.value is State.Error) _stateLiveData.postValue(State.Loaded)
		}
	}

	private fun cancelErrorDismiss()
	{
		errorDismissJob?.cancel()
	}

	fun refreshProducts() = productsResource.reload()
}