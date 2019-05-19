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
import pl.karol202.sciorder.server.util.newStringId

fun Route.putProduct(productDao: ProductDao) = put {
	val product = call.receive<Product>().overrideId()
	if(!product.isValid()) return@put badRequest()
	productDao.addProduct(product)
	call.respond(HttpStatusCode.Created, product)
}

private fun Product.overrideId() = copy(_id = newStringId<Product>())
