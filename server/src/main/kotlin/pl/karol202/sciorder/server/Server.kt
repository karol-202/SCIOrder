package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.routing.routing
import pl.karol202.sciorder.server.dao.DatabaseOrderDao
import pl.karol202.sciorder.server.dao.DatabaseProductDao
import pl.karol202.sciorder.server.routes.createOrder
import pl.karol202.sciorder.server.routes.createProduct
import pl.karol202.sciorder.server.routes.getOrders
import pl.karol202.sciorder.server.routes.getProducts

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
    getOrders(orderDao)
    createOrder(productDao, orderDao)

    getProducts(productDao)
    createProduct(productDao)
}
