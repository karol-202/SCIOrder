package pl.karol202.sciorder.client.common.repository.resource

import pl.karol202.sciorder.client.common.extensions.TimeUnit
import pl.karol202.sciorder.client.common.extensions.currentTimeMillis

// Class is not thread safe!
class UpdateTimeout(timeout: Int, timeUnit: TimeUnit)
{
	private val timeoutMillis = timeout.toLong() * timeUnit.millis

	private var lastUpdate: Long? = null

	fun shouldUpdate(): Boolean
	{
		val savedLastUpdate = lastUpdate
		val now = currentTimeMillis()
		if(savedLastUpdate == null || now - savedLastUpdate > timeoutMillis)
		{
			lastUpdate = now
			return true
		}
		return false
	}
}
