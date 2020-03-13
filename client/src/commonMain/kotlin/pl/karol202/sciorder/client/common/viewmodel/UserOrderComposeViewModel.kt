package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderEntryRequest
import pl.karol202.sciorder.common.request.OrderRequest

abstract class UserOrderComposeViewModel(userAuthRepository: UserAuthRepository,
                                         private val orderRepository: OrderRepository) : ViewModel()
{
	enum class OrderResult
	{
		SUCCESS, FAILURE
	}
	
	private val userAuthFlow = userAuthRepository.getUserAuthFlow()

	private val orderEntriesChannel = ConflatedBroadcastChannel<List<OrderedProduct>>(emptyList())
	private val orderResultEventChannel = ConflatedBroadcastChannel<Event<OrderResult>>()
	
	protected val orderFlow = orderEntriesChannel.asFlow()
	protected val errorEventFlow = orderResultEventChannel.asFlow()
	
	val orderEntriesOrNull get() = orderEntriesChannel.value.takeIf { it.isNotEmpty() }

	fun addToOrder(orderedProduct: OrderedProduct)
	{
		val oldProducts = orderEntriesChannel.value
		orderEntriesChannel.offer(oldProducts + orderedProduct)
	}

	fun replaceInOrder(oldProduct: OrderedProduct, newProduct: OrderedProduct)
	{
		val oldProducts = orderEntriesChannel.value
		val newProducts = oldProducts.map { if(it == oldProduct) newProduct else it }
		orderEntriesChannel.offer(newProducts)
	}

	fun removeFromOrder(orderedProduct: OrderedProduct)
	{
		val oldProducts = orderEntriesChannel.value
		orderEntriesChannel.offer(oldProducts - orderedProduct)
	}

	fun orderAll(details: Order.Details)
	{
		val orderedProducts = orderEntriesOrNull ?: return
		val entries = orderedProducts.map { OrderEntryRequest(it.product.id, it.quantity, it.parameters) }
		executeOrder(OrderRequest(entries, details))

		orderEntriesChannel.offer(emptyList())
	}

	fun orderSingleProduct(orderedProduct: OrderedProduct, details: Order.Details)
	{
		val entry = with(orderedProduct) { OrderEntryRequest(product.id, quantity, parameters) }
		executeOrder(OrderRequest(listOf(entry), details))
	}

	private fun executeOrder(order: OrderRequest) = launch {
		val authData = userAuthFlow.first() ?: return@launch
		orderRepository.addOrder(authData.authToken, authData.store.id, order).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { orderResultEventChannel.offer(Event(OrderResult.SUCCESS)) },
			     onError = { orderResultEventChannel.offer(Event(OrderResult.FAILURE)) })

	override fun onCleared()
	{
		super.onCleared()
		orderEntriesChannel.cancel()
		orderResultEventChannel.cancel()
	}
}