package pl.karol202.sciorder.server.routes.product

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.put
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.database.ProductDao
import pl.karol202.sciorder.server.extensions.isValid
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.notFound
import pl.karol202.sciorder.server.util.ok

fun Route.putProduct(productDao: ProductDao) = put {
	val ownerId = call.parameters["ownerId"] ?: return@put badRequest()
	val productId = call.parameters["productId"] ?: return@put badRequest()
	val product = call.receive<Product>().override(ownerId)
	if(!product.isValid() || product.id != productId) return@put badRequest()
	val success = productDao.updateProduct(ownerId, product)
	if(success) ok() else notFound()
}

private fun Product.override(ownerId: String) = copy(ownerId = ownerId)
