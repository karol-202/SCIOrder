package pl.karol202.sciorder.server.controller.user

import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.controller.RequestHandler
import pl.karol202.sciorder.server.controller.created
import pl.karol202.sciorder.server.controller.ok
import pl.karol202.sciorder.server.controller.requireBody
import pl.karol202.sciorder.server.service.user.UserService

class UserControllerImpl(private val userService: UserService) : UserController
{
	override suspend fun postUser(requestHandler: RequestHandler) = requestHandler {
		val user = requireBody<UserRequest> { isValid }
		
		val newUser = userService.insertUser(user)
		created(newUser)
	}
	
	override suspend fun loginUser(requestHandler: RequestHandler) = requestHandler {
		val loginRequest = requireBody<UserLoginRequest>()
		
		val result = userService.loginUser(loginRequest)
		ok(result)
	}
}
