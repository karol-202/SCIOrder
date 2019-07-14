package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.flow.map
import kotlinx.serialization.set
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.js.common.model.local.FakeDao.IdUniqueElement
import pl.karol202.sciorder.common.Order

class LocalOrderDao : LocalDao<Set<IdUniqueElement<Order>>>(LocalStorage.STORAGE_ORDERS,
                                                            IdUniqueElement.serializer(Order.serializer()).set,
                                                            emptySet()),
                      OrderDao, FakeDao
{
	override suspend fun insert(items: List<Order>) = setData { it + items.wrap() }

	override suspend fun update(items: List<Order>) = setData { it.update(items.wrap()) }

	override suspend fun updateStatus(id: String, status: Order.Status) = setData { orders ->
		val order = orders.singleOrNull { it.id == id }?.value ?: noValue()
		orders.update(order.copy(status = status).wrap())
	}

	override suspend fun delete(items: List<Order>) = setData { it - items.wrap() }

	override suspend fun deleteAll() = setData { emptySet() }

	override fun getAll() = getFromStorage().map { it.values() }

	override fun getByOwnerId(ownerId: String) = getAll().map { orders ->
		orders.filter { it.ownerId == ownerId }
	}
}
