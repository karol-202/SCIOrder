package pl.karol202.sciorder.server.controller.product.parameter

import pl.karol202.sciorder.server.controller.RequestHandler

interface ProductParameterController
{
	suspend fun postParameter(handler: RequestHandler)
	
	suspend fun putParameter(handler: RequestHandler)
	
	suspend fun deleteParameter(handler: RequestHandler)
	
	suspend fun getParameters(handler: RequestHandler)
}
