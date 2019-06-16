package pl.karol202.sciorder.client.common.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.extensions.asLiveData

abstract class CoroutineViewModel private constructor(private val job: Job,
                                                      protected val coroutineScope: CoroutineScope = CoroutineScope(job)) :
		ViewModel()
{
	constructor() : this(Job())

	override fun onCleared() = job.cancel()

	protected fun <T> Flow<T>.asLiveData() = asLiveData(coroutineScope)

	protected fun <T> BroadcastChannel<T>.asLiveData() = asLiveData(coroutineScope)

	protected fun launch(block: suspend CoroutineScope.() -> Unit)
	{
		coroutineScope.launch { block() }
	}
}
