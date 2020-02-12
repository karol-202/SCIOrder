package pl.karol202.sciorder.server.controller.product

import pl.karol202.sciorder.server.controller.RequestHandler

interface ProductController
{
	suspend fun postProduct(handler: RequestHandler)
	
	suspend fun getProducts(handler: RequestHandler)
}
