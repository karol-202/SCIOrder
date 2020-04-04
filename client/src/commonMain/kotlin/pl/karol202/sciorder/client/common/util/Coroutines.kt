package pl.karol202.sciorder.client.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

fun <E> ConflatedBroadcastChannel<E>.sendNow(element: E) = offer(element).let { Unit }

fun <T> Flow<T>.conflatedBroadcastIn(scope: CoroutineScope, start: CoroutineStart = CoroutineStart.LAZY) =
		conflate().broadcastIn(scope, start) as ConflatedBroadcastChannel<T>

fun <T> Flow<T>.observe(coroutineScope: CoroutineScope, observer: (T) -> Unit)
{
	coroutineScope.launch {
		collect { observer(it) }
	}
}

fun <T> Flow<Event<T>>.observeEvent(coroutineScope: CoroutineScope, observer: (T) -> Unit) =
		observe(coroutineScope) { it.getIfNotConsumed()?.let(observer) }
