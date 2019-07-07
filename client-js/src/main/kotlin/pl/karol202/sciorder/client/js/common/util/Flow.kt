package pl.karol202.sciorder.client.js.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.util.Event

fun <T> Flow<T>.shareIn(coroutineScope: CoroutineScope) =
		conflate().broadcastIn(coroutineScope).asFlow()

fun <T> Flow<T>.observe(coroutineScope: CoroutineScope, observer: (T) -> Unit) =
		coroutineScope.launch {
			collect { observer(it) }
		}

fun <T> Flow<Event<T>>.observeEvent(coroutineScope: CoroutineScope, observer: (T) -> Unit) =
		observe(coroutineScope) { it.getIfNotConsumed()?.let(observer) }
