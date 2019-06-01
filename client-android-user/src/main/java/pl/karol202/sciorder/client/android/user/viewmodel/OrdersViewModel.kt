package pl.karol202.sciorder.client.android.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pl.karol202.sciorder.client.android.common.components.CoroutineViewModel
import pl.karol202.sciorder.client.android.common.components.Event
import pl.karol202.sciorder.client.android.common.extensions.MutableLiveData
import pl.karol202.sciorder.client.android.common.extensions.create
import pl.karol202.sciorder.client.android.common.extensions.handleResponse
import pl.karol202.sciorder.client.android.common.extensions.observeOnceNonNull
import pl.karol202.sciorder.client.android.common.model.OrderedProduct
import pl.karol202.sciorder.client.android.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.android.common.model.local.owner.OwnerDao
import pl.karol202.sciorder.client.android.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.android.common.model.remote.OrderApi
import pl.karol202.sciorder.client.android.common.model.remote.OwnerApi
import pl.karol202.sciorder.client.android.common.repository.owner.OwnerRepositoryImpl
import pl.karol202.sciorder.client.android.user.repository.order.OrderRepositoryImpl
import pl.karol202.sciorder.common.model.Order

class OrdersViewModel(ownerDao: OwnerDao,
                      ownerApi: OwnerApi,
                      orderDao: OrderDao,
                      orderApi: OrderApi) : CoroutineViewModel()
{
	enum class OrderResult
	{
		SUCCESS, FAILURE
	}

	private val ownerRepository = OwnerRepositoryImpl(coroutineScope, ownerDao, ownerApi)
	private val orderRepository = OrderRepositoryImpl(coroutineScope, orderDao, orderApi)

	private val ownerLiveData = ownerRepository.getOwner()

	private val _orderLiveData = MutableLiveData<List<OrderedProduct>>(emptyList())
	val orderLiveData: LiveData<List<OrderedProduct>> = _orderLiveData

	private val _errorEventLiveData = MediatorLiveData<Event<OrderResult>>()
	val errorEventLiveData: LiveData<Event<OrderResult>> = _errorEventLiveData

	fun addToOrder(orderedProduct: OrderedProduct)
	{
		val oldProducts = _orderLiveData.value ?: emptyList()
		_orderLiveData.postValue(oldProducts + orderedProduct)
	}

	fun replaceInOrder(oldProduct: OrderedProduct, newProduct: OrderedProduct)
	{
		val oldProducts = _orderLiveData.value ?: emptyList()
		val newProducts = oldProducts.map { if(it == oldProduct) newProduct else it }
		_orderLiveData.postValue(newProducts)
	}

	fun removeFromOrder(orderedProduct: OrderedProduct)
	{
		val oldProducts = _orderLiveData.value ?: emptyList()
		_orderLiveData.postValue(oldProducts - orderedProduct)
	}

	fun getProductsInOrder() = _orderLiveData.value?.takeIf { it.isNotEmpty() }

	fun orderAll(details: Order.Details)
	{
		val products = getProductsInOrder() ?: return
		val entries = products.map { with(it) { Order.Entry(product.id, quantity, parameters) } }
		executeOrder(Order.create(entries, details))

		_orderLiveData.postValue(emptyList())
	}

	fun orderSingleProduct(orderedProduct: OrderedProduct, details: Order.Details)
	{
		val entry = with(orderedProduct) { Order.Entry(product.id, quantity, parameters) }
		executeOrder(Order.create(listOf(entry), details))
	}

	private fun executeOrder(order: Order)
	{
		ownerLiveData.observeOnceNonNull { owner ->
			orderRepository.executeOrder(owner, order).handleResponse()
		}
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse() =
			handleResponse(successListener = { _errorEventLiveData.value = Event(OrderResult.SUCCESS) },
			               failureListener = { _errorEventLiveData.value = Event(OrderResult.FAILURE) })
}
