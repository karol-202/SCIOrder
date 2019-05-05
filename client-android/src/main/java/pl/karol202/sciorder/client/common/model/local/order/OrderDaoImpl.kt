package pl.karol202.sciorder.client.common.model.local.order

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.karol202.sciorder.common.model.Order

fun OrderEntityDao.toOrderDao(): OrderDao = OrderDaoImpl(this)

class OrderDaoImpl(private val orderEntityDao: OrderEntityDao) : OrderDao
{
	@WorkerThread
	override fun insert(items: List<Order>) = orderEntityDao.insert(items.map { it.toOrderEntity() })

	@WorkerThread
	override fun update(items: List<Order>) = orderEntityDao.update(items.map { it.toOrderEntity() })

	@WorkerThread
	override fun updateStatus(id: String, status: Order.Status) = orderEntityDao.updateStatus(id, status)

	@WorkerThread
	override fun delete(items: List<Order>) = orderEntityDao.delete(items.map { it.toOrderEntity() })

	override fun getAll(): LiveData<List<Order>> =
		Transformations.map(orderEntityDao.getAll()) { entities -> entities.map { it.toOrder() } }

	private fun OrderEntity.toOrder() = Order(id, entries, details, status)

	private fun Order.toOrderEntity() = OrderEntity(id, entries, details, status)
}
