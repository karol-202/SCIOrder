package pl.karol202.sciorder.client.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import pl.karol202.sciorder.client.common.components.Event
import pl.karol202.sciorder.client.common.model.remote.ApiResponse

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) =
		observe(lifecycleOwner, Observer<T> { observer(it) })

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.let(observer) }

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
		observe(lifecycleOwner) { it?.getIfNotConsumed()?.let(observer) }

private class DisposableObserver<T>(private val liveData: LiveData<T>,
                                    private val observer: (T?) -> Boolean) : Observer<T>
{
	fun plug() = liveData.observeForever(this)

	override fun onChanged(value: T?)
	{
		if(observer(value)) liveData.removeObserver(this)
	}
}

// Waits for first non-null value and then stops observing
fun <T> LiveData<T>.observeOnceNonNull(observer: (T) -> Unit) =
		DisposableObserver(this) { value -> if(value != null) true.also { observer(value) } else false }.plug()

fun <T> LiveData<ApiResponse<T>>.handleResponse(targetLiveData: MediatorLiveData<*>,
                                                successListener: (T) -> Unit,
                                                failureListener: () -> Unit)
{
	targetLiveData.addSource(this) { apiResponse ->
		targetLiveData.removeSource(this)
		if(apiResponse is ApiResponse.Success) successListener(apiResponse.data)
		else failureListener()
	}
}
