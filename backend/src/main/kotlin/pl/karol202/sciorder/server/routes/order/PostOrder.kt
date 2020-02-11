package pl.karol202.sciorder.server.routes.order

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.server.dao.OrderDao
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.created
import pl.karol202.sciorder.server.util.newStringId
import pl.karol202.sciorder.server.validation.isValid

fun Route.postOrder(productDao: ProductDao, orderDao: OrderDao) = post {
	val ownerId = call.parameters["ownerId"] ?: return@post badRequest()
	val order = call.receive<Order>().overwrite(ownerId)
	if(!order.isValid(productDao)) return@post badRequest()
	orderDao.insertOrder(order)
	created(order)
}

private fun Order.overwrite(ownerId: String) = copy(_id = newStringId<Order>(),
                                                                                                         ownerId = ownerId,
                                                                                                         status = Order.Status.WAITING)
