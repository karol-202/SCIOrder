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
import pl.karol202.sciorder.server.auth.JWTProvider
import pl.karol202.sciorder.server.auth.authModule
import pl.karol202.sciorder.server.config.propertiesFromConfig
import pl.karol202.sciorder.server.controller.controllerModule
import pl.karol202.sciorder.server.controller.order.OrderController
import pl.karol202.sciorder.server.controller.product.ProductController
import pl.karol202.sciorder.server.controller.product.parameter.ProductParameterController
import pl.karol202.sciorder.server.controller.requestHandler
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
		
		jwt {
			realm = jwtProvider.realm
			verifier(jwtProvider.verifier)
			validate { jwtProvider.validate(it) }
		}
	}
}

private fun Application.routing() = routing {
	val productController by inject<ProductController>()
	val productParameterController by inject<ProductParameterController>()
	val orderController by inject<OrderController>()
	
	authenticate {
		route("api/stores") {
			//getOwner(get())
			//postOwner(get())
			
			route("{storeId}") {
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
	}
}
