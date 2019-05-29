package pl.karol202.sciorder.client.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.extensions.MutableLiveData
import pl.karol202.sciorder.client.common.extensions.create
import pl.karol202.sciorder.client.common.extensions.handleResponse
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.settings.Settings
import pl.karol202.sciorder.client.common.settings.liveString
import pl.karol202.sciorder.common.model.Order

class OrdersViewModel(private val orderDao: OrderDao,
                      private val orderApi: OrderApi,
                      settings: Settings) : ViewModel()
{
	private val coroutineJob = Job()
	private val coroutineScope = CoroutineScope(coroutineJob)

	private val _ownerIdSettingLiveData = settings.liveString("ownerId", null)
	private val ownerId get() = _ownerIdSettingLiveData.value

	private val _orderLiveData = MutableLiveData<List<OrderedProduct>>(emptyList())
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
		fun saveOrderLocally(order: Order) = coroutineScope.launch { orderDao.insert(listOf(order)) }

		val ownerId = ownerId ?: return
		orderApi.addOrder(ownerId, order).handleResponse { saveOrderLocally(it) }
	}

	private fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit) =
			handleResponse(_errorEventLiveData,
			               successListener = successListener,
			               failureListener = { _errorEventLiveData.value = Event(Unit) })
}
