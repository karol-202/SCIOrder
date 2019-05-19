package pl.karol202.sciorder.server.routes.order

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext
import pl.karol202.sciorder.server.database.OrderDao

fun Route.getOrders(orderDao: OrderDao) = get {
	val all = call.parameters["all"] == "true"
	if(all) getAllOrders(orderDao)
	else getOrdersById(orderDao)
}

private suspend fun PipelineContext<*, ApplicationCall>.getAllOrders(orderDao: OrderDao)
{
	call.respond(orderDao.getAllOrders())
}

private suspend fun PipelineContext<*, ApplicationCall>.getOrdersById(orderDao: OrderDao)
{
	val ids = call.parameters.getAll("id") ?: emptyList()
	call.respond(orderDao.getOrdersById(ids))
}
