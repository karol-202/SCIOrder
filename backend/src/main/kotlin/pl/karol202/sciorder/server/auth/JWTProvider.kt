package pl.karol202.sciorder.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

class JWTProvider(val realmAdmin: String,
                  val realmUser: String,
                  val claimAdminId: String,
                  val claimStoreId: String,
                  secret: String)
{
	private val algorithm = Algorithm.HMAC256(secret)
	val verifier: JWTVerifier = JWT.require(algorithm).build()
}
