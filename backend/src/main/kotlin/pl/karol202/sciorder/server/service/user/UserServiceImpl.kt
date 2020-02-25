package pl.karol202.sciorder.server.service.user

import pl.karol202.sciorder.common.model.User
import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest
import pl.karol202.sciorder.server.auth.JWTProvider
import pl.karol202.sciorder.server.controller.forbidden
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.entity.UserEntity
import pl.karol202.sciorder.server.table.Stores

class UserServiceImpl(private val jwtProvider: JWTProvider) : UserService
{
	override suspend fun insertUser(user: UserRequest): User
	{
		val userEntity = UserEntity.new {
			password = user.password
		}
		return userEntity.map()
	}
	
	override suspend fun loginUser(request: UserLoginRequest): String
	{
		val userEntity = UserEntity.findById(request.userId) ?: forbidden()
		if(userEntity.password != request.password) forbidden()
		
		val storeEntity = StoreEntity.find { Stores.name eq request.storeName }.limit(1).singleOrNull() ?: forbidden()
		
		return jwtProvider.signForUser(userEntity.id.value, storeEntity.id.value)
	}
}
