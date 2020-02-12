package pl.karol202.sciorder.server.controller.product

import io.ktor.application.call
import io.ktor.request.receiveOrNull
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.controller.RequestHandler
import pl.karol202.sciorder.server.service.product.ProductService
import pl.karol202.sciorder.server.util.badRequest
import pl.karol202.sciorder.server.util.ok

class ProductControllerImpl(private val productService: ProductService) : ProductController
{
	override fun postProduct(handler: RequestHandler) = handler {
		val product = call.receiveOrNull<Product>()?.takeIf { it.isValid } ?: return@handler badRequest()
	}
	
	override fun getProducts(handler: RequestHandler) = handler {
		val storeId = call.parameters["storeId"]?.toLongOrNull() ?: return@handler badRequest()
		val products = productService.getProducts(storeId)
		ok(products)
	}
}
