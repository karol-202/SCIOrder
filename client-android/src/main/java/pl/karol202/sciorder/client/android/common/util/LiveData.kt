package pl.karol202.sciorder.client.android.common.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.util.Event

private class FlowLiveData<T>(private val flow: Flow<T>,
                              private val coroutineScope: CoroutineScope) : LiveData<T>()
{
	private var collectJob: Job? = null

	override fun onActive()
	{
		collectJob = coroutineScope.launch {
			flow.collect { postValue(it) }
		}
	}

	override fun onInactive()
	{
		collectJob?.cancel()
	}
}

// LiveData creation

fun <T> Flow<T>.asLiveData(coroutineScope: CoroutineScope): LiveData<T> = FlowLiveData(this, coroutineScope)

// LiveData observing

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) = apply {
	observe(lifecycleOwner, Observer { observer(it) })
}

fun <T : Any> LiveData<out T?>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.let(observer) }

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.getIfNotConsumed()?.let(observer) }
