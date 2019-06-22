package pl.karol202.sciorder.client.common.util

import java.security.MessageDigest

actual fun String.sha1() = hash("SHA-1")

private fun String.hash(algorithm: String) =
		MessageDigest.getInstance(algorithm).digest(toByteArray()).joinToString(separator = "") { "%02x".format(it) }