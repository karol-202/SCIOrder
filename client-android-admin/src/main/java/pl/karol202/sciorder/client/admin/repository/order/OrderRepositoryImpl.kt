package pl.karol202.sciorder.client.admin.repository.order

import kotlinx.coroutines.CoroutineScope
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.repository.resource.RoomResource
import pl.karol202.sciorder.client.common.repository.resource.UpdateTimeout
import pl.karol202.sciorder.common.model.Order
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
}
