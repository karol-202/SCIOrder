package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.server.dao.Dao
import pl.karol202.sciorder.server.util.hasDuplicates
import pl.karol202.sciorder.server.util.newStringId

fun Route.createProduct(dao: Dao) = post("addProduct") {
	val product = call.receive<Product>().overrideId()
	if(!product.isValid()) return@post call.respond(HttpStatusCode.BadRequest)
	dao.addProduct(product)
	call.respond(HttpStatusCode.Created)
}

private fun Product.overrideId() = copy(_id = newStringId<Product>())

private fun Product.isValid() = parameters.all { it.isValid() } && !parameters.hasDuplicates { it.name }

private fun Product.Parameter.isValid() = when(type)
{
	Product.Parameter.Type.ENUM -> attributes.enumValues != null
	else -> true
}