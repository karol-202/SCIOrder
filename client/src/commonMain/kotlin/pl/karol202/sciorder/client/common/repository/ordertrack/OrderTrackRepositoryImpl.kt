package pl.karol202.sciorder.client.common.repository.ordertrack

import kotlinx.coroutines.delay
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.order.OrderApi
import pl.karol202.sciorder.client.common.database.dao.OrderDao
import pl.karol202.sciorder.client.common.database.dao.delete
import pl.karol202.sciorder.client.common.database.dao.insert
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.client.common.util.seconds
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Owner

class OrderTrackRepositoryImpl(private val orderDao: OrderDao,
                               private val orderApi: OrderApi) : OrderTrackRepository
{
	override fun getTrackedOrdersResource(ownerId: String) = object : DaoMixedResource<Order>(orderDao, orderDao.getByOwnerId(ownerId)) {
		override suspend fun waitForNextUpdate() = delay(10.seconds)

		override suspend fun loadFromNetwork(oldData: List<Order>) = orderApi.getOrdersByIds(ownerId, oldData.map { it.id })
	}

	override suspend fun executeOrder(owner: Owner, order: Order): ApiResponse<Order>
	{
		suspend fun saveLocally(order: Order) = orderDao.insert(order)
		suspend fun revertLocally(order: Order) = orderDao.delete(order)
		suspend fun patchLocally(patched: Order)
		{
			revertLocally(order)
			saveLocally(patched)
		}
		
		saveLocally(order)
		return orderApi.addOrder(owner.id, order).ifSuccess { patchLocally(it) }.ifFailure { revertLocally(order) }
	}

	override suspend fun removeOrder(order: Order) = orderDao.delete(listOf(order))
}
