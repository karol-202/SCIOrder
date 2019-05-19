package pl.karol202.sciorder.server.routes.order

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.database.OrderDao
import pl.karol202.sciorder.server.util.badRequest

fun Route.postOrderStatus(orderDao: OrderDao) = post {
	val orderId = call.parameters["id"] ?: return@post badRequest()
	val status = call.parameters["status"]?.let { Order.Status.getByName(it) } ?: return@post badRequest()
	val success = orderDao.updateOrderStatus(orderId, status)
	call.respond(if(success) HttpStatusCode.OK else HttpStatusCode.NotFound)
}
