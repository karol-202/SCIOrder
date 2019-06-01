package pl.karol202.sciorder.client.android.admin.repository.order

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.extensions.doOnSuccess
import pl.karol202.sciorder.client.android.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.android.common.model.remote.OrderApi
import pl.karol202.sciorder.client.android.common.repository.resource.RoomResource
import pl.karol202.sciorder.client.android.common.repository.resource.UpdateTimeout
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Owner
import java.util.concurrent.TimeUnit

class OrderRepositoryImpl(private val coroutineScope: CoroutineScope,
                          private val orderDao: OrderDao,
                          private val orderApi: OrderApi): OrderRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.SECONDS)

	override fun getAllOrders(ownerId: String, hash: String) = object : RoomResource<Order>(coroutineScope, orderDao) {
		override fun shouldFetchFromNetwork(data: List<Order>) = updateTimeout.shouldUpdate()

		override fun loadFromNetwork(oldData: List<Order>) = orderApi.getAllOrders(ownerId, hash)
	}

	override fun updateOrderStatus(owner: Owner, order: Order, status: Order.Status) =
			orderApi.updateOrderStatus(owner.id, order.id, owner.hash, status).doOnSuccess { updateOrderLocally(order, status) }

	private fun updateOrderLocally(order: Order, status: Order.Status) =
			coroutineScope.launch { orderDao.updateStatus(order.id, status) }

	override fun removeAllOrders(owner: Owner) =
			orderApi.removeAllOrders(owner.id, owner.hash).doOnSuccess { removeOrdersLocally() }

	private fun removeOrdersLocally() = coroutineScope.launch { orderDao.deleteAll() }
}
