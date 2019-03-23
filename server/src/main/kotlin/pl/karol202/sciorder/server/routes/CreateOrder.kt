package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.server.dao.Dao

fun Route.createOrder(dao: Dao) = post("createOrder") {
	val entries = call.receive<Array<Order.Entry>>().toList()
	if(!validateEntries(dao, entries)) return@post call.respond(HttpStatusCode.BadRequest)
	dao.createOrder(entries)

	call.respond(HttpStatusCode.Created)
}

private suspend fun validateEntries(dao: Dao, entries: List<Order.Entry>) =
		entries.all {
			val product = dao.getProductOfId(it.productId)
			product?.available == true
		}