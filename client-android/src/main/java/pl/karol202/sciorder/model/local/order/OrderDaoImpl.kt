package pl.karol202.sciorder.model.local.order

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.karol202.sciorder.model.Order

fun OrderEntityDao.toOrderDao() = OrderDaoImpl(this)

class OrderDaoImpl(private val orderEntityDao: OrderEntityDao) : OrderDao
{
	@WorkerThread
	override fun clearOrders() = orderEntityDao.clearOrders()

	@WorkerThread
	override fun insertOrders(orders: List<Order>) =
		orderEntityDao.insertOrders(orders.map { it.toOrderEntity() })

	override fun getAllOrders(): LiveData<List<Order>> =
		Transformations.map(orderEntityDao.getAllOrders()) { entities -> entities.map { it.toOrder() } }

	private fun OrderEntity.toOrder() = Order(id, entries, Order.Details(location, recipient), status)

	private fun Order.toOrderEntity() = OrderEntity(_id, entries, details.location, details.recipient, status)
}
