package pl.karol202.sciorder.client.common.repository.order

import kotlinx.coroutines.delay
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.remote.order.OrderApi
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.client.common.util.seconds
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Owner

class OrderRepositoryImpl(private val orderDao: OrderDao,
                          private val orderApi: OrderApi) : OrderRepository
{
	override fun getOrdersResource(ownerId: String, hash: String) = object : DaoMixedResource<Order>(orderDao) {

		override suspend fun waitForNextUpdate() = delay(10.seconds)

		override suspend fun loadFromNetwork(oldData: List<Order>) = orderApi.getAllOrders(ownerId, hash)
	}

	override suspend fun updateOrderStatus(owner: Owner, order: Order, status: Order.Status) =
			orderApi.updateOrderStatus(owner.id, order.id, owner.hash, status).ifSuccess { updateOrderLocally(order, status) }

	override suspend fun removeAllOrders(owner: Owner) =
			orderApi.removeAllOrders(owner.id, owner.hash).ifSuccess { removeOrdersLocally() }

	private suspend fun updateOrderLocally(order: Order, status: Order.Status) = orderDao.updateStatus(order.id, status)

	private suspend fun removeOrdersLocally() = orderDao.deleteAll()
}
