package pl.karol202.sciorder.client.common.util

import kotlinx.coroutines.sync.Mutex

suspend fun Mutex.tryDoLocking(owner: Any? = null, action: suspend () -> Unit)
{
	if(!tryLock(owner)) return
	try { action() }
	finally { unlock(owner) }
}
