package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import pl.karol202.sciorder.server.dao.Dao

fun Route.getProducts(dao: Dao) = get("getProducts") {
	call.respond(dao.getAllProducts())
}