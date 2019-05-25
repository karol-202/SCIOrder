package pl.karol202.sciorder.server.routes.order

import io.ktor.routing.Route
import io.ktor.routing.delete
import pl.karol202.sciorder.server.database.OrderDao
import pl.karol202.sciorder.server.util.ok

fun Route.deleteOrders(ordersDao: OrderDao) = delete {
	ordersDao.removeAllOrders()
	ok()
}
