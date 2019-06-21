package pl.karol202.sciorder.client.common.repository.ordertrack

import kotlinx.coroutines.delay
import pl.karol202.sciorder.client.common.extensions.seconds
import pl.karol202.sciorder.client.common.model.local.OrderDao
import pl.karol202.sciorder.client.common.model.remote.OrderApi
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Owner

class OrderTrackRepositoryImpl(private val orderDao: OrderDao,
                               private val orderApi: OrderApi) : OrderTrackRepository
{
	override fun getTrackedOrdersResource(ownerId: String) = object : DaoMixedResource<Order>(orderDao, orderDao.getByOwnerId(ownerId)) {
		override suspend fun waitForNextUpdate() = delay(10.seconds)

		override suspend fun loadFromNetwork(oldData: List<Order>) = orderApi.getOrdersByIds(ownerId, oldData.map { it.id })
	}

	override suspend fun executeOrder(owner: Owner, order: Order) =
			orderApi.addOrder(owner.id, order).ifSuccess { saveOrderLocally(it) }

	private suspend fun saveOrderLocally(order: Order) = orderDao.insert(listOf(order))

	override suspend fun removeOrder(order: Order) = orderDao.delete(listOf(order))
}
