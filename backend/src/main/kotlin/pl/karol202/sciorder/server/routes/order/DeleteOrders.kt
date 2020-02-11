package pl.karol202.sciorder.server.routes.order

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.delete
import pl.karol202.sciorder.server.dao.OrderDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.ok

fun Route.deleteOrders(ordersDao: OrderDao) = delete {
	val ownerId = call.parameters["ownerId"] ?: return@delete badRequest()
	ordersDao.deleteOrdersByStore(ownerId)
	ok()
}
