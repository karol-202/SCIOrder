package pl.karol202.sciorder.server.routes.order

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext
import pl.karol202.sciorder.server.database.OrderDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.ok

fun Route.getOrders(orderDao: OrderDao) = get {
	val ownerId = call.parameters["ownerId"] ?: return@get badRequest()
	val all = call.parameters["all"] == "true"
	if(all) getAllOrders(orderDao, ownerId)
	else getOrdersById(orderDao, ownerId)
}

private suspend fun PipelineContext<*, ApplicationCall>.getAllOrders(orderDao: OrderDao, ownerId: String)
{
	ok(orderDao.getOrdersByOwner(ownerId))
}

private suspend fun PipelineContext<*, ApplicationCall>.getOrdersById(orderDao: OrderDao, ownerId: String)
{
	val ids = call.parameters.getAll("id") ?: emptyList()
	ok(orderDao.getOrdersById(ownerId, ids))
}
