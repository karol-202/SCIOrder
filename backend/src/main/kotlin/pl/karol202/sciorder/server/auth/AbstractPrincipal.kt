package pl.karol202.sciorder.server.auth

import io.ktor.auth.Principal

sealed class AbstractPrincipal : Principal
{
	data class StorePrincipal(val storeId: Long) : AbstractPrincipal()
	{
		override fun hasAccessToStore(storeId: Long) = storeId == this.storeId
	}
	
	data class AdminPrincipal(val adminId: Long) : AbstractPrincipal()
	{
		override fun hasAccessToStore(storeId: Long) = true
	}
	
	abstract fun hasAccessToStore(storeId: Long): Boolean
}
