package pl.karol202.sciorder.client.common.model

import kotlinx.serialization.Serializable
import pl.karol202.sciorder.common.model.User

@Serializable
data class UserWithPassword(val user: User,
                            val password: String)
{
	val id get() = user.id
}
