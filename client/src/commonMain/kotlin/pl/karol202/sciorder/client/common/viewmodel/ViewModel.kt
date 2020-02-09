package pl.karol202.sciorder.client.common.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

expect abstract class ViewModel()
{
	val coroutineScope: CoroutineScope
	
	protected open fun onCleared()
}

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit)
{
	coroutineScope.launch { block() }
}
