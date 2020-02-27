package pl.karol202.sciorder.server.service.hash

import java.security.MessageDigest

class SHA256HashService : HashService
{
	private val digest = MessageDigest.getInstance("SHA-256")
	
	override fun hash(plain: String) = digest
			.digest(plain.toByteArray())
			.joinToString("") { "%02x".format(it) }
}
