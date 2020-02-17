package pl.karol202.sciorder.server.auth

import io.ktor.auth.Principal

sealed class AbstractPrincipal : Principal
{
	data class StorePrincipal(val storeId: Long) : AbstractPrincipal()
	
	data class AdminPrincipal(val adminId: Long) : AbstractPrincipal()
}
