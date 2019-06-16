package pl.karol202.sciorder.client.common.extensions

import java.security.MessageDigest

fun String.sha1() = hash("SHA-1")

private fun String.hash(algorithm: String) =
		MessageDigest.getInstance(algorithm).digest(toByteArray()).joinToString(separator = "") { "%02x".format(it) }
