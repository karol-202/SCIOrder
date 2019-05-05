package pl.karol202.sciorder.client.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.create
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.order.OrderApi
import pl.karol202.sciorder.common.model.Order

class OrderViewModel(private val orderDao: OrderDao,
                     private val orderApi: OrderApi) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val _orderLiveData = MutableLiveData<List<OrderedProduct>>().apply { value = emptyList() }
	val orderLiveData: LiveData<List<OrderedProduct>> = _orderLiveData

	private val _errorEventLiveData = MediatorLiveData<Event<Unit>>()
	val errorEventLiveData: LiveData<Event<Unit>> = _errorEventLiveData

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
		val entries = products.map { with(it) { Order.Entry(product._id, quantity, parameters) } }
		executeOrder(Order.create(entries, details))

		_orderLiveData.postValue(emptyList())
	}

	fun orderSingleProduct(orderedProduct: OrderedProduct, details: Order.Details)
	{
		val entry = with(orderedProduct) { Order.Entry(product._id, quantity, parameters) }
		executeOrder(Order.create(listOf(entry), details))
	}

	private fun executeOrder(order: Order)
	{
		val liveData = orderApi.addOrder(order)
		handleOrderAddResponse(liveData)
	}

	private fun handleOrderAddResponse(liveData: LiveData<ApiResponse<Order>>)
	{
		_errorEventLiveData.addSource(liveData) { apiResponse ->
			_errorEventLiveData.removeSource(liveData)
			if(apiResponse is ApiResponse.Success) saveOrderLocally(apiResponse.data)
			else _errorEventLiveData.value = Event(Unit)
		}
	}

	private fun saveOrderLocally(order: Order)
	{
		coroutineScope.launch { orderDao.insert(listOf(order)) }
	}
}
