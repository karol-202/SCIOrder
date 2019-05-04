package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.routing.route
import io.ktor.routing.routing
import pl.karol202.sciorder.server.dao.DatabaseOrderDao
import pl.karol202.sciorder.server.dao.DatabaseProductDao
import pl.karol202.sciorder.server.routes.order.getOrders
import pl.karol202.sciorder.server.routes.order.postOrderStatus
import pl.karol202.sciorder.server.routes.order.putOrder
import pl.karol202.sciorder.server.routes.product.getProducts
import pl.karol202.sciorder.server.routes.product.putProduct

val productDao = DatabaseProductDao()
val orderDao = DatabaseOrderDao()

fun Application.main()
{
    configure()
    routing()
}

private fun Application.configure()
{
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson()
    }
}

private fun Application.routing() = routing {
	route("products") {
		getProducts(productDao)
		putProduct(productDao)
	}
	route("orders") {
		getOrders(orderDao)
		putOrder(productDao, orderDao)

		route("{id}/status") {
			postOrderStatus(orderDao)
		}
	}
}
