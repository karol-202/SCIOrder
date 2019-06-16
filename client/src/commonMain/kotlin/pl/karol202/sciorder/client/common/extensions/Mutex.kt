package pl.karol202.sciorder.client.common.extensions

import kotlinx.coroutines.sync.Mutex

suspend fun Mutex.tryWithLockSuspend(owner: Any? = null, action: suspend () -> Unit): Boolean
{
	if(!tryLock(owner)) return false
	try { action() }
	finally { unlock(owner) }
	return true
}
