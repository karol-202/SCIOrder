package pl.karol202.sciorder.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.serialization.serialization
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import pl.karol202.sciorder.server.auth.JWTProvider
import pl.karol202.sciorder.server.auth.authModule
import pl.karol202.sciorder.server.config.propertiesFromConfig
import pl.karol202.sciorder.server.controller.StatusException
import pl.karol202.sciorder.server.controller.admin.AdminController
import pl.karol202.sciorder.server.controller.controllerModule
import pl.karol202.sciorder.server.controller.order.OrderController
import pl.karol202.sciorder.server.controller.product.ProductController
import pl.karol202.sciorder.server.controller.product.parameter.ProductParameterController
import pl.karol202.sciorder.server.controller.requestHandler
import pl.karol202.sciorder.server.controller.store.StoreController
import pl.karol202.sciorder.server.controller.user.UserController
import pl.karol202.sciorder.server.service.serviceModule

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
        serialization()
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
		
		jwt {
			realm = jwtProvider.realm
			verifier(jwtProvider.verifier)
			validate { jwtProvider.validate(it) }
		}
	}
	install(StatusPages) {
		StatusException.allExceptions.forEach { (klass, code) ->
			exception(klass.java) { call.respond(code) }
		}
		exception<SerializationException> { call.respond(HttpStatusCode.BadRequest, it) }
	}
}

private fun Application.routing() = routing {
	val productController by inject<ProductController>()
	val productParameterController by inject<ProductParameterController>()
	val orderController by inject<OrderController>()
	val storeController by inject<StoreController>()
	val adminController by inject<AdminController>()
	val userController by inject<UserController>()
	
	authenticate(optional = true) {
		route("api") {
			route("stores") {
				post { storeController.postStore(requestHandler) }
				
				route("{storeId}") {
					delete { storeController.deleteStore(requestHandler) }
					
					route("products") {
						get { productController.getProducts(requestHandler) }
						post { productController.postProduct(requestHandler) }
						
						route("{productId}") {
							put { productController.postProduct(requestHandler) }
							delete { productController.deleteProduct(requestHandler) }
							
							route("parameters") {
								get { productParameterController.getParameters(requestHandler) }
								post { productParameterController.postParameter(requestHandler) }
								
								route("{parameterId}") {
									put { productParameterController.putParameter(requestHandler) }
									delete { productParameterController.deleteParameter(requestHandler) }
								}
							}
						}
					}
					route("orders") {
						get { orderController.getOrders(requestHandler) }
						post { orderController.postOrder(requestHandler) }
						delete { orderController.deleteOrders(requestHandler) }
						
						route("{orderId}/status") {
							put { orderController.putOrderStatus(requestHandler) }
						}
					}
				}
			}
			route("admins") {
				post("register") { adminController.postAdmin(requestHandler) }
				post("login") { adminController.loginAdmin(requestHandler) }
				
				route("{adminId}") {
					delete { adminController.deleteAdmin(requestHandler) }
				}
			}
			route("users") {
				post("register") { userController.postUser(requestHandler) }
				post("login") { userController.loginUser(requestHandler) }
			}
		}
	}
}
