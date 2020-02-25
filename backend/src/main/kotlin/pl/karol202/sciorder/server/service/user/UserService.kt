package pl.karol202.sciorder.server.service.user

import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.request.UserRequest

interface UserService
{
	fun insertUser(user: UserRequest): User
}
