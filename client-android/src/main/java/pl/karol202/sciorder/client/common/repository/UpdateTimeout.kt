package pl.karol202.sciorder.client.common.repository

import android.os.SystemClock
import java.util.concurrent.TimeUnit

class UpdateTimeout(timeout: Int, timeUnit: TimeUnit)
{
	private val timeoutMillis = timeUnit.toMillis(timeout.toLong())

	private var lastUpdate: Long? = null

	@Synchronized
	fun shouldUpdate(): Boolean
	{
		val savedLastUpdate = lastUpdate
		val now = now()
		if(savedLastUpdate == null)
		{
			lastUpdate = now
			return true
		}
		if(now - savedLastUpdate > timeoutMillis)
		{
			lastUpdate = now
			return true
		}
		return false
	}

	private fun now() = SystemClock.uptimeMillis()
}