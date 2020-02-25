package pl.karol202.sciorder.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.jwt.JWTCredential

private const val CLAIM_ADMIN_ID = "admin_id"

private const val CLAIM_USER_ID = "user_id"
private const val CLAIM_STORE_ID = "store_id"

class JWTProvider(val realm: String,
                  secret: String)
{
	private val algorithm = Algorithm.HMAC256(secret)
	val verifier: JWTVerifier = JWT.require(algorithm).build()
	
	fun validate(credentials: JWTCredential) = validateAdminCredentials(credentials) ?: validateUserCredentials(credentials)
	
	private fun validateAdminCredentials(credentials: JWTCredential): AbstractPrincipal.AdminPrincipal?
	{
		val adminId = credentials.payload.getClaim(CLAIM_ADMIN_ID).asLong() ?: return null
		return AbstractPrincipal.AdminPrincipal(adminId)
	}
	
	private fun validateUserCredentials(credentials: JWTCredential): AbstractPrincipal.UserPrincipal?
	{
		val userId = credentials.payload.getClaim(CLAIM_USER_ID).asLong() ?: return null
		val storeId = credentials.payload.getClaim(CLAIM_STORE_ID).asLong() ?: return null
		return AbstractPrincipal.UserPrincipal(userId, storeId)
	}
	
	fun signForAdmin(adminId: Long) = JWT.create()
			.withClaim(CLAIM_ADMIN_ID, adminId)
			.sign(algorithm)
	
	fun signForUser(userId: Long, storeId: Long) = JWT.create()
			.withClaim(CLAIM_USER_ID, userId)
			.withClaim(CLAIM_STORE_ID, storeId)
			.sign(algorithm)
}
