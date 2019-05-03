package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import pl.karol202.sciorder.server.dao.OrderDao

fun Route.getOrders(orderDao: OrderDao) = get("orders") {
	val ids = call.parameters.getAll("id")
	val orders = if(ids != null) orderDao.getOrdersById(ids) else orderDao.getAllOrders()
	call.respond(orders)
}
