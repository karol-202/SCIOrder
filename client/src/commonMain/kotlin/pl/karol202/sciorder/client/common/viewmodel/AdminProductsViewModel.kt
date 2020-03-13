package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.conflatedBroadcastIn
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductRequest

abstract class AdminProductsViewModel(adminAuthRepository: AdminAuthRepository,
                                      storeRepository: StoreRepository,
                                      private val productRepository: ProductRepository) : ViewModel()
{
	enum class UpdateResult
	{
		SUCCESS, FAILURE
	}
	
	private val updateEventChannel = ConflatedBroadcastChannel<Event<UpdateResult>>()
	
	private val adminAuthFlow = adminAuthRepository.getAdminAuthFlow()
	private val selectedStoreFlow = storeRepository.getSelectedStoreFlow()
	
	private val productsResourceChannel = adminAuthFlow
			.combine(selectedStoreFlow) { adminAuth, selectedStore ->
				if(adminAuth == null || selectedStore == null) null
				else productRepository.getProductsResource(adminAuth.authToken, selectedStore.id)
			}
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
	
	protected val updateEventFlow = updateEventChannel
			.asFlow()
			.distinctUntilChanged()
	
	fun refreshProducts() = launch { productsResourceChannel.valueOrNull?.reload() }
	
	fun addProduct(product: ProductRequest) = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		val storeId = selectedStoreFlow.first()?.id ?: return@launch
		productRepository.addProduct(token, storeId, product).handleResponse()
	}
	
	fun updateProduct(productId: Long, product: ProductRequest) = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		val storeId = selectedStoreFlow.first()?.id ?: return@launch
		productRepository.updateProduct(token, storeId, productId, product).handleResponse()
	}
	
	fun removeProduct(productId: Long) = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		val storeId = selectedStoreFlow.first()?.id ?: return@launch
		productRepository.removeProduct(token, storeId, productId).handleResponse()
	}
	
	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { updateEventChannel.offer(Event(UpdateResult.SUCCESS)) },
			     onError = { updateEventChannel.offer(Event(UpdateResult.FAILURE)) })
	
	override fun onCleared()
	{
		super.onCleared()
		productsResourceChannel.valueOrNull?.close()
		productsResourceChannel.close()
		updateEventChannel.cancel()
	}
}
