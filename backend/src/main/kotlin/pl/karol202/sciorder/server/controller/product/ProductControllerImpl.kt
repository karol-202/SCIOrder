package pl.karol202.sciorder.server.controller.product

import io.ktor.application.call
import io.ktor.auth.principal
import io.ktor.request.receiveOrNull
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.controller.RequestHandler
import pl.karol202.sciorder.server.service.product.ProductService
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.forbidden
import pl.karol202.sciorder.server.util.ok
import pl.karol202.sciorder.server.util.unauthorized

class ProductControllerImpl(private val productService: ProductService) : ProductController
{
	override suspend fun postProduct(handler: RequestHandler) = handler {
		val product = call.receiveOrNull<Product>()?.takeIf { it.isValid } ?: return@handler badRequest()
	}
	
	override suspend fun getProducts(handler: RequestHandler) = handler {
		val storeId = call.parameters["storeId"]?.toLongOrNull() ?: return@handler badRequest()
		
		val principal = call.principal<AbstractPrincipal>() ?: return@handler unauthorized()
		if(!principal.hasAccessToStore(storeId)) return@handler forbidden()
		
		val products = productService.getProducts(storeId)
		ok(products)
	}
}
