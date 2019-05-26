package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import pl.karol202.sciorder.server.routes.order.deleteOrders
import pl.karol202.sciorder.server.routes.order.getOrders
import pl.karol202.sciorder.server.routes.order.putOrderStatus
import pl.karol202.sciorder.server.routes.order.postOrder
import pl.karol202.sciorder.server.routes.owner.getOwner
import pl.karol202.sciorder.server.routes.owner.putOwnerHash
import pl.karol202.sciorder.server.routes.owner.postOwner
import pl.karol202.sciorder.server.routes.product.deleteProduct
import pl.karol202.sciorder.server.routes.product.getProducts
import pl.karol202.sciorder.server.routes.product.putProduct
import pl.karol202.sciorder.server.routes.product.postProduct
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
		postOwner(get())

		route("{ownerId}") {
			route("hash") {
				putOwnerHash(get())
			}

			route("products") {
				getProducts(get())
				postProduct(get())

				route("{productId}") {
					putProduct(get())
					deleteProduct(get())
				}
			}
			route("orders") {
				getOrders(get())
				postOrder(get(), get())
				deleteOrders(get())

				route("{orderId}/status") {
					putOrderStatus(get())
				}
			}
		}
	}
}
