package pl.karol202.sciorder.server.routes.order

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.put
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.database.OrderDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.notFound
import pl.karol202.sciorder.server.util.ok

fun Route.putOrderStatus(orderDao: OrderDao) = put {
	val ownerId = call.parameters["ownerId"] ?: return@put badRequest()
	val orderId = call.parameters["orderId"] ?: return@put badRequest()
	val status = call.parameters["status"]?.let { Order.Status.getByName(it) } ?: return@put badRequest()
	val success = orderDao.updateOrderStatus(ownerId, orderId, status)
	if(success) ok() else notFound()
}
