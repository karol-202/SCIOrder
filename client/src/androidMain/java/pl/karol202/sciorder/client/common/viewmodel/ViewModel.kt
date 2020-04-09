package pl.karol202.sciorder.client.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

actual abstract class ViewModel : ViewModel()
{
	actual val coroutineScope get() = viewModelScope + Dispatchers.Default
	
	actual override fun onCleared() { }
}
