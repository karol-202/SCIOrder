package pl.karol202.sciorder.client.android.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import pl.karol202.sciorder.client.android.common.util.Event
import pl.karol202.sciorder.client.android.common.viewmodel.CoroutineViewModel
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.model.create
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.repository.ordertrack.OrderTrackRepository
import pl.karol202.sciorder.client.common.repository.owner.OwnerRepository
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Owner

class OrdersViewModel(private val ownerRepository: OwnerRepository,
                      private val orderTrackRepository: OrderTrackRepository) : CoroutineViewModel()
{
	enum class OrderResult
	{
		SUCCESS, FAILURE
	}

	private var owner: Owner? = null

	private val orderBroadcastChannel = ConflatedBroadcastChannel<List<OrderedProduct>>(emptyList())
	val orderLiveData = orderBroadcastChannel.asFlow().asLiveData()

	private val _errorEventLiveData = MediatorLiveData<Event<OrderResult>>()
	val errorEventLiveData: LiveData<Event<OrderResult>> = _errorEventLiveData

	init
	{
		collectOwner()
	}

	private fun collectOwner() = launch {
		ownerRepository.getOwnerFlow().collect { owner = it }
	}

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
		orderTrackRepository.executeOrder(owner ?: return@launch, order).handleResponse()
	}

	private suspend fun <T> ApiResponse<T>.handleResponse() =
			fold(onSuccess = { _errorEventLiveData.postValue(Event(OrderResult.SUCCESS)) },
			     onError = { _errorEventLiveData.postValue(Event(OrderResult.FAILURE)) })

	override fun onCleared()
	{
		super.onCleared()
		orderBroadcastChannel.cancel()
	}
}
