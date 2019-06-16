package pl.karol202.sciorder.client.android.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.create
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.client.common.viewmodel.CoroutineViewModel
import pl.karol202.sciorder.common.Order

class OrdersViewModel(ownerRepository: OwnerRepository,
                      private val orderTrackRepository: OrderTrackRepository) : CoroutineViewModel()
{
	enum class OrderResult
	{
		SUCCESS, FAILURE
	}

	private val ownerFlow = ownerRepository.getOwner().conflate()

	private val orderBroadcastChannel = ConflatedBroadcastChannel<List<OrderedProduct>>(emptyList())
	val orderLiveData = orderBroadcastChannel.asLiveData()

	private val _errorEventLiveData = MediatorLiveData<Event<OrderResult>>()
	val errorEventLiveData: LiveData<Event<OrderResult>> = _errorEventLiveData

	fun addToOrder(orderedProduct: OrderedProduct)
	{
		val oldProducts = orderBroadcastChannel.value
		orderBroadcastChannel.offer(oldProducts + orderedProduct)
	}

	fun replaceInOrder(oldProduct: OrderedProduct, newProduct: OrderedProduct)
	{
		val oldProducts = orderBroadcastChannel.value
		val newProducts = oldProducts.map { if(it == oldProduct) newProduct else it }
		orderBroadcastChannel.offer(newProducts)
	}

	fun removeFromOrder(orderedProduct: OrderedProduct)
	{
		val oldProducts = orderBroadcastChannel.value
		orderBroadcastChannel.offer(oldProducts - orderedProduct)
	}

	fun getProductsInOrderOrNull() = orderBroadcastChannel.value.takeIf { it.isNotEmpty() }

	fun orderAll(details: Order.Details)
	{
		val products = getProductsInOrderOrNull() ?: return
		val entries = products.map { with(it) { Order.Entry(product.id, quantity, parameters) } }
		executeOrder(Order.create(entries, details))

		orderBroadcastChannel.offer(emptyList())
	}

	fun orderSingleProduct(orderedProduct: OrderedProduct, details: Order.Details)
	{
		val entry = with(orderedProduct) { Order.Entry(product.id, quantity, parameters) }
		executeOrder(Order.create(listOf(entry), details))
	}

	private fun executeOrder(order: Order) = launch {
		val owner = ownerFlow.first() ?: return@launch
		orderTrackRepository.executeOrder(owner, order).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { _errorEventLiveData.value = Event(OrderResult.SUCCESS) },
			     onError = { _errorEventLiveData.value = Event(OrderResult.FAILURE) })
}
