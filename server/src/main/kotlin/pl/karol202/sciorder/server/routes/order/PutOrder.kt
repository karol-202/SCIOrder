package pl.karol202.sciorder.server.routes.order

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.put
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.dao.OrderDao
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.extensions.isValid
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.newStringId

fun Route.putOrder(productDao: ProductDao, orderDao: OrderDao) = put {
	val order = call.receive<Order>().override()
	if(!order.isValid(productDao)) return@put badRequest()
	orderDao.addOrder(order)
	call.respond(HttpStatusCode.Created, order)
}

private fun Order.override() = copy(_id = newStringId<Order>(), status = Order.Status.WAITING)
