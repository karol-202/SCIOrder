package pl.karol202.sciorder.server.routes.product

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.database.ProductDao
import pl.karol202.sciorder.server.extensions.isValid
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.notFound
import pl.karol202.sciorder.server.util.ok

fun Route.postProduct(productDao: ProductDao) = post {
	val ownerId = call.parameters["ownerId"] ?: return@post badRequest()
	val product = call.receive<Product>()
	if(!product.isValid() || product.ownerId != ownerId) return@post badRequest()
	val success = productDao.updateProduct(ownerId, product)
	if(success) ok() else notFound()
}
