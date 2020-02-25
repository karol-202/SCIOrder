package pl.karol202.sciorder.server.service.order

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.request.OrderEntryRequest
import pl.karol202.sciorder.common.request.OrderRequest
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.*
import pl.karol202.sciorder.server.table.Orders
import pl.karol202.sciorder.server.util.map

class OrderServiceImpl : OrderService
{
	override suspend fun insertOrder(storeId: Long, order: OrderRequest): Order
	{
		val storeEntity = StoreEntity.findById(storeId) ?: notFound()
		val orderEntity = OrderEntity.new {
			store = storeEntity
			location = order.details.location
			recipient = order.details.recipient
			status = Order.Status.WAITING
		}
		order.entries.forEach { insertOrderEntry(storeId, orderEntity, it) }
		return orderEntity.map()
	}
	
	private fun insertOrderEntry(storeId: Long, orderEntity: OrderEntity, orderEntry: OrderEntryRequest)
	{
		val productEntity = ProductEntity.findById(orderEntry.productId)?.takeIf(storeId = storeId) ?: notFound()
		val orderEntryEntity = OrderEntryEntity.new {
			order = orderEntity
			product = productEntity
			quantity = orderEntry.quantity
		}
		orderEntry.parameters.forEach { (paramId, value) ->
			insertOrderEntryParameter(storeId, orderEntry.productId, orderEntryEntity, paramId, value)
		}
	}
	
	private fun insertOrderEntryParameter(storeId: Long, productId: Long, orderEntryEntity: OrderEntryEntity,
	                                      productParameterId: Long, parameterValue: String)
	{
		val productParameterEntity = ProductParameterEntity.findById(productParameterId)
				?.takeIf(storeId = storeId, productId = productId) ?: notFound()
		OrderEntryParameterValueEntity.new {
			orderEntry = orderEntryEntity
			productParameter = productParameterEntity
			value = parameterValue
		}
	}
	
	override suspend fun updateOrderStatus(storeId: Long, orderId: Long, status: Order.Status)
	{
		val orderEntity = OrderEntity.findById(orderId)?.takeIf(storeId = storeId) ?: notFound()
		orderEntity.status = status
	}
	
	override suspend fun deleteOrders(storeId: Long)
	{
		StoreEntity.findById(storeId) ?: notFound()
		Orders.deleteWhere { Orders.storeId eq storeId }
	}
	
	override suspend fun getAllOrders(storeId: Long): List<Order>
	{
		StoreEntity.findById(storeId) ?: notFound()
		return OrderEntity.find { Orders.storeId eq storeId }.map()
	}
	
	override suspend fun getOrdersByUser(storeId: Long, userId: Long): List<Order>
	{
		StoreEntity.findById(storeId) ?: notFound()
		UserEntity.findById(userId) ?: notFound()
		return OrderEntity.find { (Orders.storeId eq storeId) and (Orders.userId eq userId) }.map()
	}
	
	private fun ProductEntity.takeIf(storeId: Long) = takeIf { it.store.id.value == storeId }
	
	private fun ProductParameterEntity.takeIf(storeId: Long, productId: Long) =
			takeIf { it.product.store.id.value == storeId && it.product.id.value == productId }
	
	private fun OrderEntity.takeIf(storeId: Long) = takeIf { it.store.id.value == storeId }
}
