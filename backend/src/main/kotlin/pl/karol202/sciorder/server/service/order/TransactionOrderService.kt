package pl.karol202.sciorder.server.service.order

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.invoke

class TransactionOrderService(private val delegate: OrderService,
                              private val transactionService: TransactionService) : OrderService
{
	override suspend fun insertOrder(storeId: Long, userId: Long?, order: OrderRequest) = transactionService {
		delegate.insertOrder(storeId, userId, order)
	}
	
	override suspend fun updateOrderStatus(storeId: Long, orderId: Long, status: Order.Status) = transactionService {
		delegate.updateOrderStatus(storeId, orderId, status)
	}
	
	override suspend fun deleteOrders(storeId: Long) = transactionService {
		delegate.deleteOrders(storeId)
	}
	
	override suspend fun getAllOrders(storeId: Long) = transactionService {
		delegate.getAllOrders(storeId)
	}
	
	override suspend fun getOrdersByUser(storeId: Long, userId: Long) = transactionService {
		delegate.getOrdersByUser(storeId, userId)
	}
}
