package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.conflatedBroadcastIn
import pl.karol202.sciorder.common.model.Product

abstract class UserProductsViewModel(userAuthRepository: UserAuthRepository,
                                     private val productRepository: ProductRepository) : ViewModel()
{
	private val userAuthFlow = userAuthRepository.getUserAuthFlow()
	
	private val productsResourceChannel = userAuthFlow
			.map { it?.let { productRepository.getProductsResource(it.authToken, it.store.id) } }
			.scan(null as Resource<List<Product>>?) { previous, current -> previous?.close(); current }
			.onEach { it?.autoReloadIn(coroutineScope) }
			.conflatedBroadcastIn(coroutineScope, start = CoroutineStart.DEFAULT)
	
	private val productsStateFlow = productsResourceChannel
			.asFlow()
			.flatMapLatest { it?.asFlow ?: flowOf(null) }
	
	protected val productsFlow = productsStateFlow
			.map { it?.data.orEmpty() }
			.distinctUntilChanged()
	
	protected val loadingFlow = productsStateFlow
			.map { it is Resource.State.Loading }
			.distinctUntilChanged()
	
	protected val loadingErrorEventFlow = productsStateFlow
			.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
			.distinctUntilChanged()

	fun refreshProducts() = launch { productsResourceChannel.valueOrNull?.reload() }
	
	override fun onCleared()
	{
		super.onCleared()
		productsResourceChannel.valueOrNull?.close()
		productsResourceChannel.close()
	}
}
