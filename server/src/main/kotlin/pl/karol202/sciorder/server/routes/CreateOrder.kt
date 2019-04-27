package pl.karol202.sciorder.server.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.server.dao.Dao
import pl.karol202.sciorder.server.util.newStringId

fun Route.createOrder(dao: Dao) = post("addOrder") {
	val order = call.receive<Order>().override()
	if(!order.isValid(dao)) return@post call.respond(HttpStatusCode.BadRequest)
	dao.addOrder(order)
	call.respond(HttpStatusCode.Created)
}

private fun Order.override() = copy(_id = newStringId<Order>(), status = Order.Status.WAITING)

private suspend fun Order.isValid(dao: Dao) = entries.all { it.isValid(dao) }

private suspend fun Order.Entry.isValid(dao: Dao): Boolean
{
	val product = dao.getProductOfId(productId) ?: return false
	return product.available && isParamsListValid(product)
}

private fun Order.Entry.isParamsListValid(product: Product) =
	product.parameters.map { it.name }.equalsIgnoreOrder(parameters.keys) && parameters.all { it.toPair().isParamValid(product) }

private fun <T : Comparable<T>> Collection<T>.equalsIgnoreOrder(other: Collection<T>) = sorted() == other.sorted()

private fun Pair<String, String>.isParamValid(product: Product): Boolean
{
	fun Product.Parameter.valueRange(): ClosedFloatingPointRange<Float>
	{
		val minValue = attributes.minimalValue ?: Float.MIN_VALUE
		val maxValue = attributes.maximalValue ?: Float.MAX_VALUE
		return minValue..maxValue
	}

	val (name, value) = this
	val productParameter = product.parameters.singleOrNull { it.name == name }
	return when(productParameter?.type)
	{
		Product.Parameter.Type.TEXT, Product.Parameter.Type.BOOL -> true
		Product.Parameter.Type.INT -> value.toIntOrNull()?.toFloat()?.let { it in productParameter.valueRange() } ?: false
		Product.Parameter.Type.FLOAT -> value.toFloatOrNull()?.let { it in productParameter.valueRange() } ?: false
		Product.Parameter.Type.ENUM -> value in (productParameter.attributes.enumValues ?: listOf())
		null -> false
	}
}