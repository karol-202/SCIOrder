package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import pl.karol202.sciorder.server.dao.OrderDao

fun Route.getOrdersById(orderDao: OrderDao) = get("ordersById") {
	val ids = call.parameters.getAll("id") ?: emptyList()
	val orders = orderDao.getOrdersById(ids)
	call.respond(orders)
}
