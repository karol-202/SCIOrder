package pl.karol202.sciorder.client.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import pl.karol202.sciorder.client.common.components.Event

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) =
		observe(lifecycleOwner, Observer<T> { observer(it) })

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.let(observer) }

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.getIfNotConsumed()?.let(observer) }
