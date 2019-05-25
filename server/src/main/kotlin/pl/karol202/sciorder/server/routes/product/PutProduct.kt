package pl.karol202.sciorder.server.routes.product

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.put
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.database.ProductDao
import pl.karol202.sciorder.server.extensions.isValid
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.created
import pl.karol202.sciorder.server.util.newStringId

fun Route.putProduct(productDao: ProductDao) = put {
	val ownerId = call.parameters["ownerId"] ?: return@put badRequest()
	val product = call.receive<Product>().overrideId(ownerId)
	if(!product.isValid()) return@put badRequest()
	productDao.insertProduct(product)
	created(product)
}

private fun Product.overrideId(ownerId: String) = copy(_id = newStringId<Product>(),
                                                       ownerId = ownerId)
