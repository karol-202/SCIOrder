package pl.karol202.sciorder.client.common.model.local.order

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.flow.asFlow
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.common.Order

fun OrderEntityDao.toOrderDao(): OrderDao = RoomOrderDao(this)

class RoomOrderDao(private val orderEntityDao: OrderEntityDao) : OrderDao
{
	override suspend fun insert(items: List<Order>) = orderEntityDao.insert(items.map { it.toOrderEntity() })

	override suspend fun update(items: List<Order>) = orderEntityDao.update(items.map { it.toOrderEntity() })

	override suspend fun updateStatus(id: String, status: Order.Status) = orderEntityDao.updateStatus(id, status)

	override suspend fun delete(items: List<Order>) = orderEntityDao.delete(items.map { it.toOrderEntity() })

	override suspend fun deleteAll() = orderEntityDao.deleteAll()

	override fun getAll() =
			orderEntityDao.getAll().asFlow().map { entities -> entities.map { it.toOrder() } }

	override fun getByOwnerId(ownerId: String) =
			orderEntityDao.getByOwnerId(ownerId).asFlow().map { entities -> entities.map { it.toOrder() } }

	private fun OrderEntity.toOrder() = Order(id, ownerId, entries, details, status)

	private fun Order.toOrderEntity() = OrderEntity(id, ownerId, entries, details, status)
}
