package pl.karol202.sciorder.client.admin.repository.order

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.common.model.local.order.OrderDao
import pl.karol202.sciorder.client.common.model.remote.order.OrderApi
import pl.karol202.sciorder.client.common.repository.Resource
import pl.karol202.sciorder.client.common.repository.RoomResource
import pl.karol202.sciorder.client.common.repository.UpdateTimeout
import java.util.concurrent.TimeUnit

class OrderRepositoryImpl(private val coroutineScope: CoroutineScope,
                          private val orderDao: OrderDao,
                          private val orderApi: OrderApi): OrderRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.SECONDS)

	override fun getAllOrders() = object : RoomResource<Order>(coroutineScope, orderDao) {
		override fun shouldFetchFromNetwork(data: List<Order>) = updateTimeout.shouldUpdate()

		override fun loadFromNetwork(oldData: List<Order>) = orderApi.getAllOrders()
	}
}
