package pl.karol202.sciorder.server.controller.user

import pl.karol202.sciorder.server.controller.RequestHandler

interface UserController
{
	suspend fun postUser(requestHandler: RequestHandler)
	
	suspend fun loginUser(requestHandler: RequestHandler)
}
