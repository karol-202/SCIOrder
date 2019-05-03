package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.util.hasDuplicates
import pl.karol202.sciorder.server.util.newStringId

fun Route.createProduct(productDao: ProductDao) = post("products") {
	val product = call.receive<Product>().overrideId()
	if(!product.isValid()) return@post call.respond(HttpStatusCode.BadRequest)
	productDao.addProduct(product)
	call.respond(HttpStatusCode.Created, product)
}

private fun Product.overrideId() = copy(_id = newStringId<Product>())

private fun Product.isValid() = parameters.all { it.isValid() } && !parameters.hasDuplicates { it.name }

private fun Product.Parameter.isValid() = with(attributes) {
	when(type)
	{
		Product.Parameter.Type.INT -> defaultValue?.let { it.toIntOrNull() != null } ?: true
		Product.Parameter.Type.FLOAT -> defaultValue?.let { it.toFloatOrNull() != null } ?: true
		Product.Parameter.Type.ENUM -> !enumValues.isNullOrEmpty() && defaultValue?.let { it in enumValues!! } ?: true
		else -> true
	}
}
