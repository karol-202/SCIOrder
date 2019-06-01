package pl.karol202.sciorder.client.android.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import pl.karol202.sciorder.client.android.common.components.Event
import pl.karol202.sciorder.client.android.common.model.remote.ApiResponse

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

fun <T : Any> LiveData<T?>.nonNull(): LiveData<T> = MediatorLiveData<T>().apply {
	addSource(this@nonNull) { t -> t?.let { value = it } }
}

fun <T> LiveData<ApiResponse<T>>.handleResponse(successListener: (T) -> Unit = { },
                                                failureListener: (ApiResponse.Error<T>) -> Unit = { }) = apply {
	observeOnceNonNull { apiResponse ->
		when(apiResponse)
		{
			is ApiResponse.Success -> successListener(apiResponse.data)
			is ApiResponse.Error -> failureListener(apiResponse)
		}
	}
}

fun <T> LiveData<ApiResponse<T>>.doOnSuccess(onSuccess: (T) -> Unit) = handleResponse(successListener = onSuccess)

fun <T> LiveData<ApiResponse<T>>.doOnFailure(onFailure: (ApiResponse.Error<T>) -> Unit) = handleResponse(failureListener = onFailure)
