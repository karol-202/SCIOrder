package pl.karol202.sciorder.client.common.repository.ordertrack

import pl.karol202.sciorder.client.common.extensions.TimeUnit
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.client.common.repository.resource.UpdateTimeout
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Owner

class OrderTrackRepositoryImpl(private val orderDao: OrderDao,
                               private val orderApi: OrderApi) : OrderTrackRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.SECONDS)

	override fun getTrackedOrdersResource(ownerId: String) = object : DaoMixedResource<Order>(orderDao, orderDao.getByOwnerId(ownerId)) {
		override fun shouldFetchFromNetwork(data: List<Order>) = data.isNotEmpty() && updateTimeout.shouldUpdate()

		override suspend fun loadFromNetwork(oldData: List<Order>) = orderApi.getOrdersByIds(ownerId, oldData.map { it.id })
	}

	override suspend fun executeOrder(owner: Owner, order: Order) =
			orderApi.addOrder(owner.id, order).ifSuccess { saveOrderLocally(it) }

	private suspend fun saveOrderLocally(order: Order) = orderDao.insert(listOf(order))

	override suspend fun removeOrder(order: Order) = orderDao.delete(listOf(order))
}
