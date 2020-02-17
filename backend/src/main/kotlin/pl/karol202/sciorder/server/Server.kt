package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpMethod
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.auth.JWTProvider
import pl.karol202.sciorder.server.auth.authModule
import pl.karol202.sciorder.server.config.propertiesFromConfig
import pl.karol202.sciorder.server.controller.controllerModule
import pl.karol202.sciorder.server.controller.product.ProductController
import pl.karol202.sciorder.server.controller.requestHandler
import pl.karol202.sciorder.server.service.serviceModule

private const val AUTH_ADMIN = "admin"
private const val AUTH_USER = "user"

@KtorExperimentalAPI
fun Application.main()
{
    configure()
    routing()
}

@KtorExperimentalAPI
private fun Application.configure()
{
	install(Koin) {
		propertiesFromConfig(environment)
		modules(listOf(authModule(), controllerModule(), serviceModule()))
	}
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
	install(Authentication) {
		val jwtProvider by inject<JWTProvider>()
		
		jwt(name = AUTH_ADMIN) {
			realm = jwtProvider.realmAdmin
			verifier(jwtProvider.verifier)
			validate { credentials ->
				val adminId: Long? = credentials.payload.getClaim(jwtProvider.claimAdminId).asLong()
				adminId?.let { AbstractPrincipal.AdminPrincipal(it) }
			}
		}
		
		jwt(name = AUTH_USER) {
			realm = jwtProvider.realmUser
			verifier(jwtProvider.verifier)
			validate { credentials ->
				val storeId: Long? = credentials.payload.getClaim(jwtProvider.claimStoreId).asLong()
				storeId?.let { AbstractPrincipal.StorePrincipal(it) }
			}
		}
	}
	
}

private fun Application.routing() = routing {
	val productController by inject<ProductController>()
	
	route("api/stores") {
		//getOwner(get())
		//postOwner(get())

		route("{storeId}") {
			route("products") {
				userAuth { get { productController.getProducts(requestHandler) } }
				adminAuth { post { productController.postProduct(requestHandler) } }

				route("{productId}") {
					adminAuth { put { productController.postProduct(requestHandler) } }
					adminAuth { delete { productController.deleteProduct(requestHandler) } }
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

private fun Route.adminAuth(block: Route.() -> Unit) = authenticate(AUTH_ADMIN, build = block)

private fun Route.userAuth(block: Route.() -> Unit) = authenticate(AUTH_USER, build = block)
