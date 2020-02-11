package pl.karol202.sciorder.server.routes.product

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.delete
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.notFound
import pl.karol202.sciorder.server.util.ok

fun Route.deleteProduct(productDao: ProductDao) = delete {
	val ownerId = call.parameters["ownerId"] ?: return@delete badRequest()
	val productId = call.parameters["productId"] ?: return@delete badRequest()
	val success = productDao.deleteProduct(ownerId, productId)
	if(success) ok() else notFound()
}
