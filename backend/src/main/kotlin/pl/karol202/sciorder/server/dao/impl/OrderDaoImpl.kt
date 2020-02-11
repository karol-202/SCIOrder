package pl.karol202.sciorder.server.dao.impl

import org.jetbrains.exposed.sql.*
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.dao.OrderDao
import pl.karol202.sciorder.server.entity.OrderEntryTable
import pl.karol202.sciorder.server.entity.OrderParameterValueTable
import pl.karol202.sciorder.server.entity.OrderTable

class OrderDaoImpl : OrderDao
{
	override suspend fun insertOrder(order: Order): Order
	{
		val orderId = OrderTable.insertAndGetId {
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
		val entryId = OrderEntryTable.insertAndGetId {
			it[orderId] = targetOrderId
			it[ordinal] = entryOrdinal
			it[productId] = entry.productId
			it[quantity] = entry.quantity
		}.value
		
		entry.parameters.entries.forEach { (paramId, paramValue) -> insertOrderEntryParameter(entryId, paramId, paramValue) }
	}
	
	private fun insertOrderEntryParameter(entryId: Int, paramId: Int, paramValue: String)
	{
		OrderParameterValueTable.insert {
			it[orderEntryId] = entryId
			it[productParameterId] = paramId
			it[value] = paramValue
		}
	}
	
	override suspend fun updateOrderStatus(orderId: Int, status: Order.Status)
	{
		OrderTable.update({ OrderTable.id eq orderId }) {
			it[OrderTable.status] = status
		}
	}
	
	override suspend fun deleteOrdersByStore(storeId: Int)
	{
		OrderTable.deleteWhere { OrderTable.storeId eq storeId }
	}
	
	override suspend fun getOrdersByStore(storeId: Int) = getOrders { OrderTable.storeId eq storeId }
	
	override suspend fun getOrdersByIds(orderIds: List<Int>) = getOrders { OrderTable.id inList orderIds }
	
	private fun getOrders(where: SqlExpressionBuilder.() -> Op<Boolean>): List<Order>
	{
		return (OrderTable leftJoin OrderEntryTable leftJoin OrderParameterValueTable)
				.select(where).toList().getOrders()
	}
	
	private fun List<ResultRow>.getOrders() = this
			.groupBy { Order(id = it[OrderTable.id].value,
			                 storeId = it[OrderTable.storeId],
			                 entries = emptyList(),
			                 details = Order.Details(location = it[OrderTable.location],
			                                         recipient = it[OrderTable.recipient]),
			                 status = it[OrderTable.status]) }
			.map { (order, rows) ->
				order.copy(entries = rows.getOrderEntries())
			}
	
	private fun List<ResultRow>.getOrderEntries() = this
			.groupBy { Order.Entry(productId = it[OrderEntryTable.productId],
			                       quantity = it[OrderEntryTable.quantity],
			                       parameters = emptyMap()) to it[OrderEntryTable.ordinal] }
			.map { (entryAndOrdinal, rows) ->
				val (entry, ordinal) = entryAndOrdinal
				entry.copy(parameters = rows.getOrderEntryParams()) to ordinal
			}
			.sortedBy { (_, ordinal) -> ordinal }
			.map { (entry, _) -> entry }
	
	private fun List<ResultRow>.getOrderEntryParams() =
			associate { it[OrderParameterValueTable.productParameterId] to it[OrderParameterValueTable.value] }
}
