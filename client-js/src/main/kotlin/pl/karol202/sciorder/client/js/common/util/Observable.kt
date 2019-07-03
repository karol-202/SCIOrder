package pl.karol202.sciorder.client.js.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.util.Event

interface Observable<T>
{
	fun observe(coroutineScope: CoroutineScope, observer: (T) -> Unit)
}

private class ConflatedBroadcastChannelObservable<T>(private val channel: ConflatedBroadcastChannel<T>) : Observable<T>
{
	override fun observe(coroutineScope: CoroutineScope, observer: (T) -> Unit)
	{
		coroutineScope.launch {
			channel.openSubscription().consumeEach(observer)
		}
	}
}

private class FlowObservable<T>(private val flow: Flow<T>) : Observable<T>
{
	override fun observe(coroutineScope: CoroutineScope, observer: (T) -> Unit)
	{
		coroutineScope.launch {
			flow.conflate().produceIn(this).consumeEach(observer)
		}
	}
}

fun <T> ConflatedBroadcastChannel<T>.asObservable(): Observable<T> = ConflatedBroadcastChannelObservable(this)

fun <T> Flow<T>.asObservable(): Observable<T> = FlowObservable(this)

fun <T> Observable<Event<T>>.observeEvent(coroutineScope: CoroutineScope, observer: (T) -> Unit) =
		observe(coroutineScope) { it.getIfNotConsumed()?.let(observer) }
