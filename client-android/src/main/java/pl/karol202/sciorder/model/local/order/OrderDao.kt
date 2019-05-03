package pl.karol202.sciorder.model.local.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Order

interface OrderDao
{
	fun insertOrder(order: Order)

	fun insertOrders(orders: List<Order>)

	fun deleteOrder(order: Order)

	fun deleteOrders()

	fun getAllOrders(): LiveData<List<Order>>
}
