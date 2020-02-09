package pl.karol202.sciorder.client.android.common.database.order

import kotlinx.coroutines.reactive.flow.asFlow
import pl.karol202.sciorder.client.common.database.order.OrderDao
import pl.karol202.sciorder.common.model.Order

fun OrderEntityDao.toOrderDao(): OrderDao = RoomOrderDao(this)

class RoomOrderDao(private val orderEntityDao: OrderEntityDao) : OrderDao
{
	override suspend fun insert(items: List<Order>) = orderEntityDao.insert(items.toOrderEntities())

	override suspend fun update(items: List<Order>) = orderEntityDao.update(items.toOrderEntities())

	override suspend fun updateStatus(id: String, status: Order.Status) = orderEntityDao.updateStatus(id, status)

	override suspend fun delete(items: List<Order>) = orderEntityDao.delete(items.toOrderEntities())

	override suspend fun deleteAll() = orderEntityDao.deleteAll()

	override fun getAll() =
			orderEntityDao.getAll().asFlow().map { it.toOrders() }

	override fun getByOwnerId(ownerId: String) =
			orderEntityDao.getByOwnerId(ownerId).asFlow().map { it.toOrders() }
	
	override fun getStatus(id: String) =
			orderEntityDao.getStatus(id).asFlow().map { it.singleOrNull() }
	
	private fun List<OrderEntity>.toOrders() = map { it.toOrder() }
	
	private fun OrderEntity.toOrder() = Order(id, ownerId, entries, details, status)

	private fun List<Order>.toOrderEntities() = map { it.toOrderEntity() }
	
	private fun Order.toOrderEntity() = OrderEntity(id, ownerId, entries, details, status)
}
