package pl.karol202.sciorder.common.model

data class UserLoginResult(val user: User,
                           val store: Store,
                           val authToken: String)
