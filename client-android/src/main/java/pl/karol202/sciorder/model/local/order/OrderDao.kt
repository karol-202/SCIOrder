package pl.karol202.sciorder.model.local.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Order

interface OrderDao
{
	fun clearOrders()

	fun insertOrders(orders: List<Order>)

	fun getAllOrders(): LiveData<List<Order>>
}
