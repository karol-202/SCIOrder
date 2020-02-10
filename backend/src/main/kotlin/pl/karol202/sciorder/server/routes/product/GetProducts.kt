package pl.karol202.sciorder.server.routes.product

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import pl.karol202.sciorder.server.database.ProductDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.ok

fun Route.getProducts(productDao: ProductDao) = get {
	val ownerId = call.parameters["ownerId"] ?: return@get badRequest()
	ok(productDao.getProductsByOwner(ownerId))
}
