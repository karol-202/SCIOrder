package pl.karol202.sciorder.server.controller.user

import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest
import pl.karol202.sciorder.common.validation.isValid
import pl.karol202.sciorder.server.controller.*
import pl.karol202.sciorder.server.service.user.UserService
import pl.karol202.sciorder.server.util.Headers

class UserControllerImpl(private val userService: UserService) : UserController
{
	override suspend fun postUser(requestHandler: RequestHandler) = requestHandler {
		val user = requireBody<UserRequest> { isValid }
		
		val newUser = userService.insertUser(user)
		created(newUser)
	}
	
	override suspend fun loginUser(requestHandler: RequestHandler) = requestHandler {
		val loginRequest = requireBody<UserLoginRequest>()
		
		val token = userService.loginUser(loginRequest)
		setHeader(Headers.WWW_AUTHENTICATE, token)
		ok()
	}
}
