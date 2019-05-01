package pl.karol202.sciorder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import pl.karol202.sciorder.components.Event
import pl.karol202.sciorder.extensions.create
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.OrderedProduct
import pl.karol202.sciorder.model.remote.ApiResponse
import pl.karol202.sciorder.model.remote.order.OrderApi

class OrderViewModel(application: Application) : AndroidViewModel(application)
{
	private val orderApi = OrderApi.create()

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

	fun isOrderListEmpty() = _orderLiveData.value?.isEmpty() ?: true

	fun orderAll()
	{
		val products = _orderLiveData.value?.takeIf { it.isNotEmpty() } ?: throw IllegalStateException()
		val entries = products.map { with(it) { Order.Entry(product._id, quantity, parameters) } }
		executeOrder(Order.create(entries))

		_orderLiveData.postValue(emptyList())
	}

	fun orderSingleProduct(orderedProduct: OrderedProduct)
	{
		val entry = with(orderedProduct) { Order.Entry(product._id, quantity, parameters) }
		executeOrder(Order.create(listOf(entry)))
	}

	private fun executeOrder(order: Order)
	{
		val liveData = orderApi.addOrder(order)
		handleResponseLiveData(liveData)
	}

	private fun handleResponseLiveData(liveData: LiveData<ApiResponse<Unit>>)
	{
		_errorEventLiveData.addSource(liveData) { apiResponse ->
			_errorEventLiveData.removeSource(liveData)
			if(apiResponse is ApiResponse.Error) _errorEventLiveData.value = Event(Unit)
		}
	}
}
