package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class CoroutineViewModel private constructor(private val job: Job,
                                                      protected val coroutineScope: CoroutineScope = CoroutineScope(job)) :
		ViewModel()
{
	constructor() : this(Job())

	override fun onCleared() = job.cancel()

	protected fun launch(block: suspend CoroutineScope.() -> Unit)
	{
		coroutineScope.launch { block() }
	}
}
