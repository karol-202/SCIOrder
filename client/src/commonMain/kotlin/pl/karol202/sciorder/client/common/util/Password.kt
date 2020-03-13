package pl.karol202.sciorder.client.common.util

import kotlin.random.Random

private val CHARS = ('A'..'Z') + ('a'..'z') + ('0'..'9')

fun generatePassword(length: Int = 32) = (1..length)
		.map { CHARS[Random.nextInt(CHARS.size)] }
		.joinToString(separator = "")
