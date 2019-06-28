package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class CoroutineViewModel : ViewModel()
{
	private val job = Job()
	protected val coroutineScope = CoroutineScope(job)

	override fun onCleared() = job.cancel()

	protected fun launch(block: suspend CoroutineScope.() -> Unit)
	{
		coroutineScope.launch { block() }
	}
}
