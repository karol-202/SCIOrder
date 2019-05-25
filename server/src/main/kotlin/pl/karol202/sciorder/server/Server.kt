package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import pl.karol202.sciorder.server.database.OrderDao
import pl.karol202.sciorder.server.database.ProductDao
import pl.karol202.sciorder.server.routes.order.deleteOrders
import pl.karol202.sciorder.server.routes.order.getOrders
import pl.karol202.sciorder.server.routes.order.postOrderStatus
import pl.karol202.sciorder.server.routes.order.putOrder
import pl.karol202.sciorder.server.routes.owner.getOwner
import pl.karol202.sciorder.server.routes.owner.postOwnerHash
import pl.karol202.sciorder.server.routes.owner.putOwner
import pl.karol202.sciorder.server.routes.product.deleteProduct
import pl.karol202.sciorder.server.routes.product.getProducts
import pl.karol202.sciorder.server.routes.product.postProduct
import pl.karol202.sciorder.server.routes.product.putProduct
import pl.karol202.sciorder.server.util.propertiesByKtorEnvironment

const val ARG_MONGODB = "mongodb.uri"

@KtorExperimentalAPI
fun Application.main()
{
    configure()
    routing()
}

@KtorExperimentalAPI
private fun Application.configure()
{
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson()
    }
	install(Koin) {
		propertiesByKtorEnvironment(environment, ARG_MONGODB)
		modules(KoinModules.databaseModule())
	}
}

private fun Application.routing() = routing {
	route("owner") {
		getOwner(get())
		putOwner(get())

		route("{ownerId}") {
			postOwnerHash(get())

			route("products") {
				getProducts(get())
				putProduct(get())
				postProduct(get())

				route("{productId}") {
					deleteProduct(get())
				}
			}
			route("orders") {
				getOrders(get())
				putOrder(get(), get())
				deleteOrders(get())

				route("{orderId}/status") {
					postOrderStatus(get())
				}
			}
		}
	}
}
