package pl.karol202.sciorder.client.common.model.local.order

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.karol202.sciorder.common.model.Order

fun OrderEntityDao.toOrderDao(): OrderDao = OrderDaoImpl(this)

class OrderDaoImpl(private val orderEntityDao: OrderEntityDao) : OrderDao
{
	@WorkerThread
	override fun insertOrders(orders: List<Order>) =
		orderEntityDao.insertOrders(orders.map { it.toOrderEntity() })

	@WorkerThread
	override fun updateOrders(orders: List<Order>) = orderEntityDao.updateOrders(orders.map { it.toOrderEntity() })

	override fun updateOrderStatus(id: String, status: Order.Status) = orderEntityDao.updateOrderStatus(id, status)

	@WorkerThread
	override fun deleteOrders(orders: List<Order>) = orderEntityDao.deleteOrders(orders.map { it.toOrderEntity() })

	@WorkerThread
	override fun deleteOrders() = orderEntityDao.deleteOrders()

	override fun getAllOrders(): LiveData<List<Order>> =
		Transformations.map(orderEntityDao.getAllOrders()) { entities -> entities.map { it.toOrder() } }

	private fun OrderEntity.toOrder() = Order(id, entries, details, status)

	private fun Order.toOrderEntity() = OrderEntity(_id, entries, details, status)
}
