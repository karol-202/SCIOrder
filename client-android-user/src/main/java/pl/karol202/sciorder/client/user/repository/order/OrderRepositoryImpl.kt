package pl.karol202.sciorder.client.user.repository.order

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.user.model.local.order.OrderDao
import pl.karol202.sciorder.client.user.model.remote.order.OrderApi
import pl.karol202.sciorder.client.user.repository.Resource
import pl.karol202.sciorder.client.user.repository.UpdateTimeout
import java.util.concurrent.TimeUnit

class OrderRepositoryImpl(private val coroutineScope: CoroutineScope,
                          private val orderDao: OrderDao,
                          private val orderApi: OrderApi): OrderRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.SECONDS)

	override fun getTrackedOrders() = object : Resource<List<Order>>() {
		override fun shouldFetchFromNetwork(data: List<Order>) = data.isNotEmpty() && updateTimeout.shouldUpdate()

		override fun loadFromDatabase() = orderDao.getAllOrders()

		override fun loadFromNetwork(oldData: List<Order>) = orderApi.getOrders(oldData.map { it._id })

		override fun saveToDatabase(data: List<Order>)
		{
			// It may be required for Dao not to be called on main thread
			coroutineScope.launch {
				orderDao.deleteOrders()
				orderDao.insertOrders(data)
			}
		}
	}
}
