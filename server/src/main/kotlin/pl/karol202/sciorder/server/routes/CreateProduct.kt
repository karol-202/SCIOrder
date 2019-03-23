package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.server.dao.Dao

fun Route.createProduct(dao: Dao) = post("createProduct") {
	val name = call.parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
	val available = call.parameters["available"]?.toBoolean() ?: true
	val parameters = call.receive<Array<Product.Parameter>>().toList()
	dao.createProduct(name, available, parameters)

	call.respond(HttpStatusCode.Created)
}