package pl.karol202.sciorder.server.routes.order

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.put
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.database.OrderDao
import pl.karol202.sciorder.server.database.ProductDao
import pl.karol202.sciorder.server.extensions.isValid
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.created
import pl.karol202.sciorder.server.util.newStringId

fun Route.postOrder(productDao: ProductDao, orderDao: OrderDao) = post {
	val ownerId = call.parameters["ownerId"] ?: return@post badRequest()
	val order = call.receive<Order>().override(ownerId)
	if(!order.isValid(productDao)) return@post badRequest()
	orderDao.insertOrder(order)
	created(order)
}

private fun Order.override(ownerId: String) = copy(_id = newStringId<Order>(),
                                                   ownerId = ownerId,
                                                   status = Order.Status.WAITING)
