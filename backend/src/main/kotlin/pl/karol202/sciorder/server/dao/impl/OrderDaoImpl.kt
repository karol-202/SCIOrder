package pl.karol202.sciorder.server.dao.impl

import org.jetbrains.exposed.sql.*
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.dao.OrderDao
import pl.karol202.sciorder.server.table.OrderEntries
import pl.karol202.sciorder.server.table.OrderEntryParameterValues
import pl.karol202.sciorder.server.table.Orders

class OrderDaoImpl : OrderDao
{
	override suspend fun insertOrder(order: Order): Order
	{
		val orderId = Orders.insertAndGetId {
			it[storeId] = order.storeId
			it[location] = order.details.location
			it[recipient] = order.details.recipient
			it[status] = order.status
		}.value
		
		order.entries.forEachIndexed { ordinal, entry -> insertOrderEntry(orderId, ordinal, entry) }
		
		return order.copy(id = orderId)
	}
	
	private fun insertOrderEntry(targetOrderId: Int, entryOrdinal: Int, entry: Order.Entry)
	{
		val entryId = OrderEntries.insertAndGetId {
			it[orderId] = targetOrderId
			it[ordinal] = entryOrdinal
			it[productId] = entry.productId
			it[quantity] = entry.quantity
		}.value
		
		entry.parameters.entries.forEach { (paramId, paramValue) -> insertOrderEntryParameter(entryId, paramId, paramValue) }
	}
	
	private fun insertOrderEntryParameter(entryId: Int, paramId: Int, paramValue: String)
	{
		OrderEntryParameterValues.insert {
			it[orderEntryId] = entryId
			it[productParameterId] = paramId
			it[value] = paramValue
		}
	}
	
	override suspend fun updateOrderStatus(orderId: Int, status: Order.Status)
	{
		Orders.update({ Orders.id eq orderId }) {
			it[Orders.status] = status
		}
	}
	
	override suspend fun deleteOrdersByStore(storeId: Int)
	{
		Orders.deleteWhere { Orders.storeId eq storeId }
	}
	
	override suspend fun getOrdersByStore(storeId: Int) = getOrders { Orders.storeId eq storeId }
	
	override suspend fun getOrdersByIds(orderIds: List<Int>) = getOrders { Orders.id inList orderIds }
	
	private fun getOrders(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Order>
	{
		return (Orders leftJoin OrderEntries leftJoin OrderEntryParameterValues)
				.select(where).toList().getOrders()
	}
	
	private fun List<ResultRow>.getOrders() = this
			.groupBy { Order(id = it[Orders.id].value,
			                 storeId = it[Orders.storeId],
			                 entries = emptyList(),
			                 details = Order.Details(location = it[Orders.location],
			                                         recipient = it[Orders.recipient]),
			                 status = it[Orders.status]) }
			.map { (order, rows) ->
				order.copy(entries = rows.getOrderEntries())
			}
	
	private fun List<ResultRow>.getOrderEntries() = this
			.groupBy { Order.Entry(productId = it[OrderEntries.productId],
			                       quantity = it[OrderEntries.quantity],
			                       parameters = emptyMap()) to it[OrderEntries.ordinal] }
			.map { (entryAndOrdinal, rows) ->
				val (entry, ordinal) = entryAndOrdinal
				entry.copy(parameters = rows.getOrderEntryParams()) to ordinal
			}
			.sortedBy { (_, ordinal) -> ordinal }
			.map { (entry, _) -> entry }
	
	private fun List<ResultRow>.getOrderEntryParams() =
			associate { it[OrderEntryParameterValues.productParameterId] to it[OrderEntryParameterValues.value] }
}
