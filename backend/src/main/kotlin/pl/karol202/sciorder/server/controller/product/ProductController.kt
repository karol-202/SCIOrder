package pl.karol202.sciorder.server.controller.product

import pl.karol202.sciorder.server.controller.RequestHandler

interface ProductController
{
	fun postProduct(handler: RequestHandler)
	
	fun getProducts(handler: RequestHandler)
}
