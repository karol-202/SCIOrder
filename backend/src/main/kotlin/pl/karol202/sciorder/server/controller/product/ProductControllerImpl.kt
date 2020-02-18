package pl.karol202.sciorder.server.controller.product

import pl.karol202.sciorder.common.request.ProductRequest
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.controller.*
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.product.ProductService

class ProductControllerImpl(private val permissionService: PermissionService,
                            private val productService: ProductService) : ProductController
{
	override suspend fun postProduct(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		val product = requireBody<ProductRequest> { isValid }
		requirePrincipal { permissionService.canInsertProduct(it, storeId) }
		
		val newProduct = productService.insertProduct(storeId, product)
		created(newProduct)
	}
	
	override suspend fun putProduct(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		val productId = requireLongParameter("productId")
		val product = requireBody<ProductRequest> { isValid }
		requirePrincipal { permissionService.canUpdateProduct(it, storeId) }
		
		productService.updateProduct(storeId, productId, product)
		ok()
	}
	
	override suspend fun deleteProduct(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		val productId = requireLongParameter("productId")
		requirePrincipal { permissionService.canDeleteProduct(it, storeId) }
		
		productService.deleteProduct(storeId, productId)
		ok()
	}
	
	override suspend fun getProducts(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		requirePrincipal { permissionService.canGetProducts(it, storeId) }
		
		val products = productService.getProducts(storeId)
		ok(products)
	}
}
