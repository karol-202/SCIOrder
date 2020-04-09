package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.model.OrderEntryWithProduct
import pl.karol202.sciorder.client.common.repository.auth.user.UserAuthRepository
import pl.karol202.sciorder.client.common.repository.order.OrderRepository
import pl.karol202.sciorder.client.common.util.Event
import pl.karol202.sciorder.client.common.util.sendNow
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

	private val orderEntriesChannel = ConflatedBroadcastChannel<List<OrderEntryWithProduct>>(emptyList())
	private val orderResultEventChannel = ConflatedBroadcastChannel<Event<OrderResult>>()
	
	protected val orderFlow = orderEntriesChannel.asFlow()
	protected val errorEventFlow = orderResultEventChannel.asFlow()
	
	val orderEntriesOrNull get() = orderEntriesChannel.value.takeIf { it.isNotEmpty() }

	fun addToOrder(orderedProduct: OrderEntryWithProduct)
	{
		val oldProducts = orderEntriesChannel.value
		orderEntriesChannel.sendNow(oldProducts + orderedProduct)
	}

	fun replaceInOrder(oldProduct: OrderEntryWithProduct, newProduct: OrderEntryWithProduct)
	{
		val oldProducts = orderEntriesChannel.value
		val newProducts = oldProducts.map { if(it == oldProduct) newProduct else it }
		orderEntriesChannel.sendNow(newProducts)
	}

	fun removeFromOrder(orderedProduct: OrderEntryWithProduct)
	{
		val oldProducts = orderEntriesChannel.value
		orderEntriesChannel.sendNow(oldProducts - orderedProduct)
	}

	fun orderAll(details: Order.Details)
	{
		val orderedProducts = orderEntriesOrNull ?: return
		
		val entries = orderedProducts.map { OrderEntryRequest(it.product!!.id, it.quantity, it.parameters) } // TODO Nullability
		executeOrder(OrderRequest(entries, details))

		orderEntriesChannel.sendNow(emptyList())
	}

	fun orderSingleProduct(orderedProduct: OrderEntryWithProduct, details: Order.Details)
	{
		val entry = with(orderedProduct) { OrderEntryRequest(product!!.id, quantity, parameters) } // TODO Nullability
		executeOrder(OrderRequest(listOf(entry), details))
	}

	private fun executeOrder(order: OrderRequest) = launch {
		val authData = userAuthFlow.first() ?: return@launch
		orderRepository.addOrder(authData.authToken, authData.store.id, order).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { orderResultEventChannel.sendNow(Event(OrderResult.SUCCESS)) },
			     onError = { orderResultEventChannel.sendNow(Event(OrderResult.FAILURE)) })

	override fun onCleared()
	{
		super.onCleared()
		orderEntriesChannel.cancel()
		orderResultEventChannel.cancel()
	}
}
