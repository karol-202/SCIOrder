package pl.karol202.sciorder.client.common.util

import kotlinx.coroutines.sync.Mutex

inline fun Mutex.tryDoLocking(owner: Any? = null, action: () -> Unit)
{
	if(!tryLock(owner)) return
	try { action() }
	finally { unlock(owner) }
}
