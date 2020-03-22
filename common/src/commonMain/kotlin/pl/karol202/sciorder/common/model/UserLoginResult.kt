package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResult(val user: User,
                           val store: Store,
                           val authToken: String)
