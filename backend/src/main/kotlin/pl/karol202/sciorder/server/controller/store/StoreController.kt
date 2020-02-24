package pl.karol202.sciorder.server.controller.store

import pl.karol202.sciorder.server.controller.RequestHandler

interface StoreController
{
	suspend fun postStore(requestHandler: RequestHandler)
	
	suspend fun deleteStore(requestHandler: RequestHandler)
}
