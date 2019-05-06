package pl.karol202.sciorder.server.routes.product

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.util.badRequest

fun Route.deleteProduct(productDao: ProductDao) = delete {
	val productId = call.parameters["id"] ?: return@delete badRequest()
	val success = productDao.removeProduct(productId)
	call.respond(if(success) HttpStatusCode.OK else HttpStatusCode.NotFound)
}
