package pl.karol202.sciorder.server.controller.admin

import pl.karol202.sciorder.server.controller.RequestHandler

interface AdminController
{
	suspend fun postAdmin(requestHandler: RequestHandler)
	
	suspend fun deleteAdmin(requestHandler: RequestHandler)
	
	suspend fun loginAdmin(requestHandler: RequestHandler)
}
