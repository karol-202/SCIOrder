package pl.karol202.sciorder.client.user.components

class Event<T>(private val content: T)
{
	var consumed = false

	@Synchronized
	fun getIfNotConsumed() = if(!consumed) content.also { consumed = true } else null
}
