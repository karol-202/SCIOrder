package pl.karol202.sciorder.client.common.model.local.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Order

interface OrderDao
{
	fun insertOrders(orders: List<Order>)

	fun updateOrders(orders: List<Order>)

	fun updateOrderStatus(id: String, status: Order.Status)

	fun deleteOrders(orders: List<Order>)

	fun deleteOrders()

	fun getAllOrders(): LiveData<List<Order>>
}
