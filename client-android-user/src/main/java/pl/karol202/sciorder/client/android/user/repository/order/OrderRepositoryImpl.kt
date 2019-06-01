package pl.karol202.sciorder.client.android.user.repository.order

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
                          private val orderApi: OrderApi) : OrderRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.SECONDS)

	override fun getTrackedOrders(ownerId: String) =
			object : RoomResource<Order>(coroutineScope, orderDao, orderDao.getByOwnerId(ownerId)) {
				override fun shouldFetchFromNetwork(data: List<Order>) = data.isNotEmpty() && updateTimeout.shouldUpdate()

				override fun loadFromNetwork(oldData: List<Order>) = orderApi.getOrdersByIds(ownerId, oldData.map { it.id })
			}

	override fun executeOrder(owner: Owner, order: Order) =
			orderApi.addOrder(owner.id, order).doOnSuccess { saveOrderLocally(it) }

	private fun saveOrderLocally(order: Order) = coroutineScope.launch { orderDao.insert(listOf(order)) }
}
