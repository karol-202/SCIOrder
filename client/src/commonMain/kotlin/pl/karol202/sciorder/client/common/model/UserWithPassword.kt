package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.model.User

data class UserWithPassword(val user: User,
                            val password: String)
{
	val id get() = user.id
}
