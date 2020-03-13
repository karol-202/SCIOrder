package pl.karol202.sciorder.client.common.repository.order

import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.ApiResponse.Error.Type.LOCAL_INCONSISTENCY
import pl.karol202.sciorder.client.common.api.order.OrderApi
import pl.karol202.sciorder.client.common.database.dao.OrderDao
import pl.karol202.sciorder.client.common.database.dao.insert
import pl.karol202.sciorder.client.common.repository.resource.StandardMixedResource
import pl.karol202.sciorder.client.common.util.seconds
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest

class OrderRepositoryImpl(private val orderDao: OrderDao,
                          private val orderApi: OrderApi) : OrderRepository
{
	override fun getOrdersResource(token: String, storeId: Long) =
			StandardMixedResource(dao = orderDao,
			                      databaseProvider = { getByStoreId(storeId) },
			                      apiProvider = { orderApi.getOrders(token, storeId) },
			                      updateIntervalMillis = 10.seconds)
	
	override suspend fun addOrder(token: String, storeId: Long, order: OrderRequest): ApiResponse<Order>
	{
		suspend fun saveLocally(order: Order) = orderDao.insert(order)
		
		return orderApi.addOrder(token, storeId, order).ifSuccess { saveLocally(it) }
	}

	override suspend fun updateOrderStatus(token: String, storeId: Long, orderId: Long, status: Order.Status): ApiResponse<Unit>
	{
		val previousStatus = orderDao.getById(orderId).first()?.status ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)

		suspend fun updateLocally() = orderDao.updateStatus(orderId, status)
		suspend fun revertLocally() = orderDao.updateStatus(orderId, previousStatus)
		
		updateLocally()
		return orderApi.updateOrderStatus(token, storeId, orderId, status).ifFailure { revertLocally() }
	}
	
	override suspend fun removeAllOrders(token: String, storeId: Long): ApiResponse<Unit>
	{
		val orders = orderDao.getByStoreId(storeId).first()
		
		suspend fun removeLocally() = orderDao.deleteByStoreId(storeId)
		suspend fun revertLocally() = orderDao.insert(orders)
		
		removeLocally()
		return orderApi.removeAllOrders(token, storeId).ifFailure { revertLocally() }
	}
	
	override suspend fun cleanLocalOrders() = orderDao.deleteAll()
}
