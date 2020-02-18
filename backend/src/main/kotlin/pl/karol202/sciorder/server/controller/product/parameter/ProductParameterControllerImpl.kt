package pl.karol202.sciorder.server.controller.product.parameter

import pl.karol202.sciorder.common.request.ProductParameterRequest
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.controller.*
import pl.karol202.sciorder.server.service.permission.PermissionService
import pl.karol202.sciorder.server.service.product.parameter.ProductParameterService

class ProductParameterControllerImpl(private val permissionService: PermissionService,
                                     private val productParameterService: ProductParameterService) : ProductParameterController
{
	override suspend fun postParameter(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		val productId = requireLongParameter("productId")
		val parameter = requireBody<ProductParameterRequest> { isValid }
		requirePrincipal { permissionService.canInsertProductParameter(it, storeId) }
		
		val newParameter = productParameterService.insertParameter(storeId, productId, parameter)
		created(newParameter)
	}
	
	override suspend fun putParameter(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		val productId = requireLongParameter("productId")
		val parameterId = requireLongParameter("parameterId")
		val parameter = requireBody<ProductParameterRequest> { isValid }
		requirePrincipal { permissionService.canUpdateProductParameter(it, storeId) }
		
		productParameterService.updateParameter(storeId, productId, parameterId, parameter)
		ok()
	}
	
	override suspend fun deleteParameter(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		val productId = requireLongParameter("productId")
		val parameterId = requireLongParameter("parameterId")
		requirePrincipal { permissionService.canDeleteProductParameter(it, storeId) }
		
		productParameterService.deleteParameter(storeId, productId, parameterId)
		ok()
	}
	
	override suspend fun getParameters(handler: RequestHandler) = handler {
		val storeId = requireLongParameter("storeId")
		val productId = requireLongParameter("productId")
		requirePrincipal { permissionService.canGetProductParameters(it, storeId) }
		
		val products = productParameterService.getParameters(storeId, productId)
		ok(products)
	}
}
