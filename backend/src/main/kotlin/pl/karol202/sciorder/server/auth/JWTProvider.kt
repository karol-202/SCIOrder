package pl.karol202.sciorder.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.jwt.JWTCredential

private const val CLAIM_ADMIN_ID = "admin_id"
private const val CLAIM_STORE_ID = "store_id"

class JWTProvider(val realm: String,
                  secret: String)
{
	private val algorithm = Algorithm.HMAC256(secret)
	val verifier: JWTVerifier = JWT.require(algorithm).build()
	
	fun validate(credentials: JWTCredential) = validateAdminCredentials(credentials) ?: validateStoreCredentials(credentials)
	
	private fun validateAdminCredentials(credentials: JWTCredential) =
			credentials.payload.getClaim(CLAIM_ADMIN_ID).asLong()?.let { AbstractPrincipal.AdminPrincipal(it) }
	
	private fun validateStoreCredentials(credentials: JWTCredential) =
			credentials.payload.getClaim(CLAIM_STORE_ID).asLong()?.let { AbstractPrincipal.StorePrincipal(it) }
}
