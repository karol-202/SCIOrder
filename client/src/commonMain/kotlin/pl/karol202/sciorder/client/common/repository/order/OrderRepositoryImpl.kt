package pl.karol202.sciorder.client.common.repository.order

import pl.karol202.sciorder.client.common.extensions.TimeUnit
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.client.common.repository.resource.UpdateTimeout
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Owner

class OrderRepositoryImpl(private val orderDao: OrderDao,
                          private val orderApi: OrderApi) : OrderRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.SECONDS)

	override fun getOrdersResource(ownerId: String, hash: String) = object : DaoMixedResource<Order>(orderDao) {
		override fun shouldFetchFromNetwork(data: List<Order>) = updateTimeout.shouldUpdate()

		override suspend fun loadFromNetwork(oldData: List<Order>) = orderApi.getAllOrders(ownerId, hash)
	}

	override suspend fun updateOrderStatus(owner: Owner, order: Order, status: Order.Status) =
			orderApi.updateOrderStatus(owner.id, order.id, owner.hash, status).ifSuccess { updateOrderLocally(order, status) }

	override suspend fun removeAllOrders(owner: Owner) =
			orderApi.removeAllOrders(owner.id, owner.hash).ifSuccess { removeOrdersLocally() }

	private suspend fun updateOrderLocally(order: Order, status: Order.Status) = orderDao.updateStatus(order.id, status)

	private suspend fun removeOrdersLocally() = orderDao.deleteAll()
}
