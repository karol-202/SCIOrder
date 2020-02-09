package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

actual abstract class ViewModel
{
	protected actual val coroutineScope = CoroutineScope(SupervisorJob())
	
	fun clear()
	{
		coroutineScope.cancel()
		onCleared()
	}
	
	protected actual open fun onCleared() { }
}
