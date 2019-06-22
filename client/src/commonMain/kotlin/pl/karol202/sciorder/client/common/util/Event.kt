package pl.karol202.sciorder.client.common.util

import kotlin.jvm.Synchronized

class Event<T>(private val content: T)
{
	var consumed = false

	@Synchronized
	fun getIfNotConsumed() = if(!consumed) content.also { consumed = true } else null
}
