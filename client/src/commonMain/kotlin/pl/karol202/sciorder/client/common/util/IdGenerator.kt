package pl.karol202.sciorder.client.common.util

import kotlin.jvm.Synchronized

class IdGenerator(start: Long = 0)
{
	private var counter = start
	
	@Synchronized
	fun nextId() = counter++
}
