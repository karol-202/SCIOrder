package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpMethod
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import pl.karol202.sciorder.server.controller.controllerModule
import pl.karol202.sciorder.server.controller.product.ProductController
import pl.karol202.sciorder.server.service.serviceModule
import pl.karol202.sciorder.server.util.propertiesByKtorEnvironment

const val ARG_DB = "database.uri"

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
	install(CORS) {
		anyHost()
		method(HttpMethod.Get)
		method(HttpMethod.Post)
		method(HttpMethod.Put)
		method(HttpMethod.Delete)
	}
	install(Koin) {
		propertiesByKtorEnvironment(environment, ARG_DB)
		modules(listOf(controllerModule(), serviceModule()))
	}
}

private fun Application.routing() = routing {
	val productController by inject<ProductController>()
	
	route("api/stores") {
		//getOwner(get())
		//postOwner(get())

		route("{storeId}") {
			route("hash") {
				//putOwnerHash(get()) // Admin-only
			}

			route("products") {
				productController.getProducts { get(it) }
				//postProduct(get()) // Admin-only

				route("{productId}") {
					//putProduct(get()) // Admin-only
					//deleteProduct(get()) // Admin-only
				}
			}
			route("orders") {
				//getOrders(get()) // Admin-only (when 'all' present)
				//postOrder(get(), get())
				//deleteOrders(get()) // Admin-only

				route("{orderId}/status") {
					//putOrderStatus(get()) // Admin-only
				}
			}
		}
	}
}
