package pl.karol202.sciorder.server.routes.product

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.dao.ProductDao

fun Route.postProduct(productDao: ProductDao) = post {
	val product = call.receive<Product>()
	val success = productDao.updateProduct(product)
	call.respond(if(success) HttpStatusCode.OK else HttpStatusCode.NotFound)
}
