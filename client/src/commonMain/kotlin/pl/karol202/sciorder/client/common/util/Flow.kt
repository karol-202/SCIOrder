package pl.karol202.sciorder.client.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun <T> Flow<T>.shareIn(coroutineScope: CoroutineScope) =
		conflate().broadcastIn(coroutineScope, CoroutineStart.DEFAULT).asFlow()

fun <T> Flow<T>.observe(coroutineScope: CoroutineScope, observer: (T) -> Unit)
{
	coroutineScope.launch {
		collect { observer(it) }
	}
}

fun <T> Flow<Event<T>>.observeEvent(coroutineScope: CoroutineScope, observer: (T) -> Unit) =
		observe(coroutineScope) { it.getIfNotConsumed()?.let(observer) }
