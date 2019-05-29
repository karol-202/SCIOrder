package pl.karol202.sciorder.client.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.model.remote.ApiResponse

private class DisposableObserver<T>(private val liveData: LiveData<T>,
                                    private val observer: (T?) -> Boolean) : Observer<T>
{
	fun plug() = liveData.observeForever(this)

	override fun onChanged(value: T?)
	{
		if(observer(value)) liveData.removeObserver(this)
	}
}

fun <T> MutableLiveData(initialValue: T) = androidx.lifecycle.MutableLiveData<T>().apply { value = initialValue }

fun <T> MediatorLiveData(initialValue: T) = MediatorLiveData<T>().apply { value = initialValue }

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) =
		observe(lifecycleOwner, Observer<T> { observer(it) })

fun <T : Any> LiveData<out T?>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.let(observer) }

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.getIfNotConsumed()?.let(observer) }

// Waits for first non-null value and then stops observing
fun <T : Any> LiveData<out T?>.observeOnceNonNull(observer: (T) -> Unit) =
		DisposableObserver(this) { value -> if(value != null) true.also { observer(value) } else false }.plug()

// Works same as Transformations.map
// Rewritten in order to ensure null safety
fun <X, Y> LiveData<X>.map(function: (X) -> Y): LiveData<Y> = MediatorLiveData<Y>().apply {
	addSource(this@map) { x -> value = function(x) }
}

fun <X, Y : Any> LiveData<X>.mapNotNull(function: (X) -> Y?): LiveData<Y> = MediatorLiveData<Y>().apply {
	addSource(this@mapNotNull) { x -> function(x)?.let { value = it } }
}

// Works same as Transformations.switchMap
// Rewritten in order to ensure null safety
fun <X, Y> LiveData<X>.switchMap(switchFunction: (X) -> LiveData<Y>): LiveData<Y> = MediatorLiveData<Y>().apply {
	var source: LiveData<Y>? = null
	addSource(this@switchMap) { x ->
		val newLiveData = switchFunction(x)
		if(newLiveData == source) return@addSource
		source?.let { removeSource(it) }
		source = newLiveData
		source?.let { addSource(it) { y -> value = y } }
	}
}

operator fun <X, Y> LiveData<X>.plus(second: LiveData<Y>): LiveData<Pair<X, Y>> = MediatorLiveData<Pair<X, Y>>().apply {
	addSource(this@plus) { x: X ->
		val y = second.value ?: return@addSource
		value = x to y
	}
	addSource(second) { y: Y ->
		val x = this@plus.value ?: return@addSource
		value = x to y
	}
}

fun <T : Any> LiveData<T?>.nonNull(): LiveData<T> = MediatorLiveData<T>().apply {
	addSource(this@nonNull) { t -> t?.let { value = it } }
}

fun <T> LiveData<ApiResponse<T>>.handleResponse(targetLiveData: MediatorLiveData<*>,
                                                successListener: (T) -> Unit,
                                                failureListener: (ApiResponse.Error<T>) -> Unit)
{
	targetLiveData.addSource(this) { apiResponse ->
		targetLiveData.removeSource(this)
		when(apiResponse)
		{
			is ApiResponse.Success -> successListener(apiResponse.data)
			is ApiResponse.Error -> failureListener(apiResponse)
		}
	}
}
