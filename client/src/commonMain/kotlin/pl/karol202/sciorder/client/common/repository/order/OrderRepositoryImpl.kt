package pl.karol202.sciorder.client.common.repository.order

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.ApiResponse.Error.Type.LOCAL_INCONSISTENCY
import pl.karol202.sciorder.client.common.api.order.OrderApi
import pl.karol202.sciorder.client.common.database.dao.OrderDao
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.client.common.util.seconds
import pl.karol202.sciorder.common.model.Order

class OrderRepositoryImpl(private val orderDao: OrderDao,
                          private val orderApi: OrderApi) : OrderRepository
{
	override fun getOrdersResource(ownerId: String, hash: String) = object : DaoMixedResource<Order>(orderDao) {
		override suspend fun waitForNextUpdate() = delay(10.seconds)

		override suspend fun loadFromNetwork(oldData: List<Order>) = orderApi.getAllOrders(ownerId, hash)
	}

	override suspend fun updateOrderStatus(owner: Owner, order: Order, status: Order.Status): ApiResponse<Unit>
	{
		val previousStatus = orderDao.getStatus(order.id).first() ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)

		suspend fun updateLocally() = orderDao.updateStatus(order.id, status)
		suspend fun revertLocally() = orderDao.updateStatus(order.id, previousStatus)
		
		updateLocally()
		return orderApi.updateOrderStatus(owner.id, order.id, owner.hash, status).ifFailure { revertLocally() }
	}
	
	override suspend fun removeAllOrders(owner: Owner): ApiResponse<Unit>
	{
		val orders = orderDao.getAll().first()
		
		suspend fun removeLocally() = orderDao.deleteAll()
		suspend fun revertLocally() = orderDao.insert(orders)
		
		removeLocally()
		return orderApi.removeAllOrders(owner.id, owner.hash).ifFailure { revertLocally() }
	}
}
