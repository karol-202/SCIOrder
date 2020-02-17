package pl.karol202.sciorder.server.controller.product

import io.ktor.application.call
import io.ktor.auth.principal
import io.ktor.request.receiveOrNull
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.auth.AbstractPrincipal
import pl.karol202.sciorder.server.controller.RequestHandler
import pl.karol202.sciorder.server.controller.StatusException
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.product.ProductService
import pl.karol202.sciorder.server.util.created
import pl.karol202.sciorder.server.util.ok

class ProductControllerImpl(private val permissionService: PermissionService,
                            private val productService: ProductService) : ProductController
{
	override suspend fun postProduct(handler: RequestHandler) = handler {
		val storeId = call.parameters["storeId"]?.toLongOrNull() ?: throw StatusException.BadRequest()
		val product = call.receiveOrNull<Product>()?.takeIf { it.isValid } ?: throw StatusException.BadRequest()
		
		val principal = call.principal<AbstractPrincipal>() ?: throw StatusException.Unauthorized()
		if(!permissionService.canInsertProduct(principal, storeId)) throw StatusException.Forbidden()
		
		val newProduct = productService.insertProduct(storeId, product)
		created(newProduct)
	}
	
	override suspend fun putProduct(handler: RequestHandler) = handler {
		val storeId = call.parameters["storeId"]?.toLongOrNull() ?: throw StatusException.BadRequest()
		val productId = call.parameters["productId"]?.toLongOrNull() ?: throw StatusException.BadRequest()
		val product = call.receiveOrNull<Product>()?.takeIf { it.isValid } ?: throw StatusException.BadRequest()
		
		val principal = call.principal<AbstractPrincipal>() ?: throw StatusException.Unauthorized()
		if(!permissionService.canPutProduct(principal, storeId)) throw StatusException.Forbidden()
		
		productService.putProduct(storeId, productId, product)
		ok()
	}
	
	override suspend fun deleteProduct(handler: RequestHandler) = handler {
		val storeId = call.parameters["storeId"]?.toLongOrNull() ?: throw StatusException.BadRequest()
		val productId = call.parameters["productId"]?.toLongOrNull() ?: throw StatusException.BadRequest()
		
		val principal = call.principal<AbstractPrincipal>() ?: throw StatusException.Unauthorized()
		if(!permissionService.canDeleteProducts(principal, storeId)) throw StatusException.Forbidden()
		
		productService.deleteProduct(storeId, productId)
		ok()
	}
	
	override suspend fun getProducts(handler: RequestHandler) = handler {
		val storeId = call.parameters["storeId"]?.toLongOrNull() ?: throw StatusException.BadRequest()
		
		val principal = call.principal<AbstractPrincipal>() ?: throw StatusException.Unauthorized()
		if(!permissionService.canGetProducts(principal, storeId)) throw StatusException.Forbidden()
		
		val products = productService.getProducts(storeId)
		ok(products)
	}
}
