package pl.karol202.sciorder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import pl.karol202.sciorder.components.Event
import pl.karol202.sciorder.extensions.create
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.model.remote.ApiResponse
import pl.karol202.sciorder.model.remote.order.OrderApi

class OrderViewModel(application: Application) : AndroidViewModel(application)
{
	private val orderApi = OrderApi.create()

	private val _errorEventLiveData = MediatorLiveData<Event<Unit>>()
	val errorEventLiveData: LiveData<Event<Unit>> = _errorEventLiveData

	fun orderProduct(product: Product, quantity: Int, parameters: Map<String, String>)
	{
		val entry = Order.Entry(product._id, quantity, parameters)
		val order = Order.create(listOf(entry))
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