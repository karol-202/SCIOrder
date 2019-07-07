package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.request.ApplicationRequest
import io.ktor.request.httpMethod
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import pl.karol202.sciorder.server.database.OwnerDao
import pl.karol202.sciorder.server.routes.order.deleteOrders
import pl.karol202.sciorder.server.routes.order.getOrders
import pl.karol202.sciorder.server.routes.order.postOrder
import pl.karol202.sciorder.server.routes.order.putOrderStatus
import pl.karol202.sciorder.server.routes.owner.getOwner
import pl.karol202.sciorder.server.routes.owner.postOwner
import pl.karol202.sciorder.server.routes.owner.putOwnerHash
import pl.karol202.sciorder.server.routes.product.deleteProduct
import pl.karol202.sciorder.server.routes.product.getProducts
import pl.karol202.sciorder.server.routes.product.postProduct
import pl.karol202.sciorder.server.routes.product.putProduct
import pl.karol202.sciorder.server.util.forbidden
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
	install(CORS) {
		anyHost()
		method(HttpMethod.Get)
		method(HttpMethod.Post)
		method(HttpMethod.Put)
		method(HttpMethod.Delete)
		header(HttpHeaders.UserAgent)
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
				protect()
				putOwnerHash(get()) // Admin-only
			}

			route("products") {
				protect { it.httpMethod == HttpMethod.Post }
				getProducts(get())
				postProduct(get()) // Admin-only

				route("{productId}") {
					protect()
					putProduct(get()) // Admin-only
					deleteProduct(get()) // Admin-only
				}
			}
			route("orders") {
				protect { (it.httpMethod == HttpMethod.Get && it.queryParameters["all"] == "true") ||
						   it.httpMethod == HttpMethod.Delete
				}
				getOrders(get()) // Admin-only (when 'all' present)
				postOrder(get(), get())
				deleteOrders(get()) // Admin-only

				route("{orderId}/status") {
					protect()
					putOrderStatus(get()) // Admin-only
				}
			}
		}
	}
}

private fun Route.protect(adminOnly: (ApplicationRequest) -> Boolean = { true }) = intercept(ApplicationCallPipeline.Features) {
	suspend fun forbid()
	{
		forbidden()
		finish()
	}

	if(!adminOnly(call.request)) return@intercept
	val ownerDao: OwnerDao by this@protect.inject()
	val ownerId = call.parameters["ownerId"] ?: throw IllegalArgumentException("Protection needs ownerId to be present")
	val hash = call.parameters["hash"]

	val owner = ownerDao.getOwnerById(ownerId) ?: return@intercept forbid()
	if(owner.hash != hash) return@intercept forbid()
}
