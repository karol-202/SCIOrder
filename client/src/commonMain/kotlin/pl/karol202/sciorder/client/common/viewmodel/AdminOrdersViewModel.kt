package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.model.OrderWithProducts
import pl.karol202.sciorder.client.common.repository.auth.admin.AdminAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.repository.product.ProductRepository
import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.client.common.repository.store.StoreRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.sendNow
import pl.karol202.sciorder.common.model.Order

abstract class AdminOrdersViewModel(adminAuthRepository: AdminAuthRepository,
                                    storeRepository: StoreRepository,
                                    private val orderRepository: OrderRepository,
                                    private val productRepository: ProductRepository) : ViewModel()
{
	companion object
	{
		val DEFAULT_FILTER = Order.Status.values().toSet() - Order.Status.DONE - Order.Status.REJECTED
	}
	
	private val filterChannel = ConflatedBroadcastChannel(DEFAULT_FILTER)
	private val updateErrorEventChannel = ConflatedBroadcastChannel<Event<Unit>>()
	
	private val adminAuthFlow = adminAuthRepository.getAdminAuthFlow()
	private val selectedStoreFlow = storeRepository.getSelectedStoreFlow()
	
	private var ordersResource: Resource<List<OrderWithProducts>>? = null
	private val ordersResourceChannel = adminAuthFlow
			.combine(selectedStoreFlow) { adminAuth, selectedStore ->
				if(adminAuth == null || selectedStore == null) null
				else orderRepository.getOrdersResource(adminAuth.authToken, selectedStore.id)
			}
			.scan(null as Resource<List<OrderWithProducts>>?) { previous, current -> previous?.close(); current }
			.onEach { ordersResource = it }
			.onEach { it?.autoReloadIn(coroutineScope) }
			.conflate()
			.broadcastIn(coroutineScope, CoroutineStart.DEFAULT)
	
	private val ordersStateFlow = ordersResourceChannel
			.asFlow()
			.flatMapLatest { it?.asFlow ?: flowOf(null) }
	
	protected val ordersFlow = ordersStateFlow
			.map { it?.data.orEmpty() }
			.combine(filterChannel.asFlow()) { orders, filter -> orders.filter { it.status in filter } }
			.distinctUntilChanged()
	
	protected val loadingFlow = ordersStateFlow
			.map { it is Resource.State.Loading }
			.distinctUntilChanged()
	
	protected val loadingErrorEventFlow = ordersStateFlow
			.mapNotNull { if(it is Resource.State.Failure) Event(Unit) else null }
			.distinctUntilChanged()
	
	protected val updateErrorEventFlow = updateErrorEventChannel
			.asFlow()
			.distinctUntilChanged()
	
	var orderFilter: Set<Order.Status>
		get() = filterChannel.value
		set(value) { filterChannel.sendNow(value) }

	fun refreshOrders() = launch { ordersResource?.reload() }

	fun updateOrderStatus(orderId: Long, status: Order.Status) = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		val storeId = selectedStoreFlow.first()?.id ?: return@launch
		orderRepository.updateOrderStatus(token, storeId, orderId, status).handleError()
	}

	fun removeAllOrders() = launch {
		val token = adminAuthFlow.first()?.authToken ?: return@launch
		val storeId = selectedStoreFlow.first()?.id ?: return@launch
		orderRepository.removeAllOrders(token, storeId).handleError()
	}

	private suspend fun <T> ApiResponse<T>.handleError() = ifFailure { updateErrorEventChannel.sendNow(Event(Unit)) }

	override fun onCleared() = launch {
		super.onCleared()
		ordersResource?.close()
		ordersResourceChannel.cancel()
		filterChannel.cancel()
		updateErrorEventChannel.cancel()
	}
}
