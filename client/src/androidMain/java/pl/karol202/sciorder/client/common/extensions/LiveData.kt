package pl.karol202.sciorder.client.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.components.Event

// LiveData creation

fun <T> MutableLiveData(initialValue: T) = androidx.lifecycle.MutableLiveData<T>().apply { value = initialValue }

fun <T> Flow<T>.asLiveData(coroutineScope: CoroutineScope) = androidx.lifecycle.MutableLiveData<T>().apply {
	coroutineScope.launch {
		collect { postValue(it) }
	}
}

fun <T> BroadcastChannel<T>.asLiveData(coroutineScope: CoroutineScope) = asFlow().asLiveData(coroutineScope)

// LiveData observing

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) = apply {
	observe(lifecycleOwner, Observer<T> { observer(it) })
}

fun <T : Any> LiveData<out T?>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.let(observer) }

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.getIfNotConsumed()?.let(observer) }

// Waits for first non-null value and then stops observing
fun <T : Any> LiveData<out T?>.observeOnceNonNull(observer: (T) -> Unit) = apply {
	DisposableObserver(this) { value -> if(value != null) true.also { observer(value) } else false }.plug()
}

private class DisposableObserver<T>(private val liveData: LiveData<T>,
                                    private val observer: (T?) -> Boolean) : Observer<T>
{
	fun plug() = liveData.observeForever(this)

	override fun onChanged(value: T?)
	{
		if(observer(value)) liveData.removeObserver(this)
	}
}
