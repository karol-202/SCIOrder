package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import pl.karol202.sciorder.server.dao.ProductDao

fun Route.getProducts(productDao: ProductDao) = get("products") {
	call.respond(productDao.getAllProducts())
}
