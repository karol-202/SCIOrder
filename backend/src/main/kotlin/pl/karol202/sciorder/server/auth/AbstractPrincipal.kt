package pl.karol202.sciorder.server.auth

import io.ktor.auth.Principal

sealed class AbstractPrincipal : Principal
{
	data class AdminPrincipal(val adminId: Long) : AbstractPrincipal()
	
	data class UserPrincipal(val userId: Long,
	                         val storeId: Long) : AbstractPrincipal()
}
