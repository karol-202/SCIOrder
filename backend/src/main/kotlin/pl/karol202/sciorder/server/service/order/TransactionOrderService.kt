package pl.karol202.sciorder.server.service.order

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService

class TransactionOrderService(private val delegate: OrderService,
                              private val transactionService: TransactionService) : OrderService
{
	override suspend fun insertOrder(storeId: Long, order: OrderRequest) = transactionService.runTransaction {
		delegate.insertOrder(storeId, order)
	}
	
	override suspend fun updateOrderStatus(storeId: Long, orderId: Long, status: Order.Status) = transactionService.runTransaction {
		delegate.updateOrderStatus(storeId, orderId, status)
	}
	
	override suspend fun deleteOrders(storeId: Long) = transactionService.runTransaction {
		delegate.deleteOrders(storeId)
	}
	
	override suspend fun getAllOrders(storeId: Long) = transactionService.runTransaction {
		delegate.getAllOrders(storeId)
	}
	
	override suspend fun getOrdersByUser(storeId: Long, userId: Long) = transactionService.runTransaction {
		delegate.getOrdersByUser(storeId, userId)
	}
}
