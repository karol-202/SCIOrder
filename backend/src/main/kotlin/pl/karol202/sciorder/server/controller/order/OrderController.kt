package pl.karol202.sciorder.server.controller.order

import pl.karol202.sciorder.server.controller.RequestHandler

interface OrderController
{
	suspend fun postOrder(requestHandler: RequestHandler)
	
	suspend fun putOrderStatus(requestHandler: RequestHandler)
	
	suspend fun deleteOrders(requestHandler: RequestHandler)
	
	suspend fun getOrders(requestHandler: RequestHandler)
}
