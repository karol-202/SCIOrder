package pl.karol202.sciorder.common.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminLoginResult(val admin: Admin,
                            val authToken: String)
