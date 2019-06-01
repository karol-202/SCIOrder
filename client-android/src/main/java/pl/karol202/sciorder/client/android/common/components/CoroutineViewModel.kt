package pl.karol202.sciorder.client.android.common.components

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class CoroutineViewModel private constructor(private val job: Job,
                                                      protected val coroutineScope: CoroutineScope = CoroutineScope(job)) :
		ViewModel(), CoroutineScope by coroutineScope
{
	constructor() : this(Job())

	override fun onCleared() = job.cancel()
}
