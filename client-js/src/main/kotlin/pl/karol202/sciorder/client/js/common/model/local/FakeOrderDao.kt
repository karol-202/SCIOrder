package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowViaChannel
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.js.common.model.local.FakeDao.IdUniqueElement
import pl.karol202.sciorder.client.js.common.util.Delegates
import pl.karol202.sciorder.client.js.common.util.invokeEach
import pl.karol202.sciorder.common.Order

class FakeOrderDao : OrderDao, FakeDao
{
	private var updateListeners = listOf<(List<Order>) -> Unit>()
	private var orders by Delegates.observable(setOf<IdUniqueElement<Order>>()) { updateListeners.invokeEach(it.values()) }

	override suspend fun insert(items: List<Order>)
	{
		orders += items.wrap()
	}

	override suspend fun update(items: List<Order>)
	{
		orders = orders.update(items.wrap())
	}

	override suspend fun updateStatus(id: String, status: Order.Status)
	{
		val order = orders.singleOrNull { it.id == id }?.value ?: return
		orders = orders.update(order.copy(status = status).wrap())
	}

	override suspend fun delete(items: List<Order>)
	{
		orders -= items.wrap()
	}

	override suspend fun deleteAll()
	{
		orders = emptySet()
	}

	override fun getAll() = flowViaChannel<List<Order>>(Channel.UNLIMITED) { channel ->
		val listener: (List<Order>) -> Unit = { channel.offer(it) }
		updateListeners += listener
		channel.invokeOnClose { updateListeners -= listener }
	}

	override fun getByOwnerId(ownerId: String) = getAll().map { orders ->
		orders.filter { it.ownerId == ownerId }
	}
}
