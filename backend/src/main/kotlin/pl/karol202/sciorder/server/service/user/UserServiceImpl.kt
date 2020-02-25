package pl.karol202.sciorder.server.service.user

import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.request.UserRequest
import pl.karol202.sciorder.server.entity.UserEntity

class UserServiceImpl : UserService
{
	override fun insertUser(user: UserRequest): User
	{
		val userEntity = UserEntity.new {
			password = user.password
		}
		return userEntity.map()
	}
}
