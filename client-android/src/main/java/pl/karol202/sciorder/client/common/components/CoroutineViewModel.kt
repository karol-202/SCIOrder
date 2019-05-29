package pl.karol202.sciorder.client.common.components

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class CoroutineViewModel(private val job: Job = Job()) : ViewModel(), CoroutineScope by CoroutineScope(job)
{
	override fun onCleared() = job.cancel()
}
