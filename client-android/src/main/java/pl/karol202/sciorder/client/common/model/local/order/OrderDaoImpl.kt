package pl.karol202.sciorder.client.common.model.local.order

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.common.extensions.map
import pl.karol202.sciorder.common.model.Order

fun OrderEntityDao.toOrderDao(): OrderDao = OrderDaoImpl(this)

class OrderDaoImpl(private val orderEntityDao: OrderEntityDao) : OrderDao
{
	override suspend fun insert(items: List<Order>) = orderEntityDao.insert(items.map { it.toOrderEntity() })

	override suspend fun update(items: List<Order>) = orderEntityDao.update(items.map { it.toOrderEntity() })

	override suspend fun updateStatus(id: String, status: Order.Status) = orderEntityDao.updateStatus(id, status)

	override suspend fun delete(items: List<Order>) = orderEntityDao.delete(items.map { it.toOrderEntity() })

	override suspend fun deleteAll() = orderEntityDao.deleteAll()

	override fun getAll(): LiveData<List<Order>> = orderEntityDao.getAll().map { entities -> entities.map { it.toOrder() } }

	override fun getByOwnerId(ownerId: String): LiveData<List<Order>> =
			orderEntityDao.getByOwnerId(ownerId).map { entities -> entities.map { it.toOrder() } }

	private fun OrderEntity.toOrder() = Order(id, ownerId, entries, details, status)

	private fun Order.toOrderEntity() = OrderEntity(id, ownerId, entries, details, status)
}
