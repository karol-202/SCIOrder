package pl.karol202.sciorder.client.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// Typealiasing to Android's ViewModel is impossible due to inconsistency between Java and Kotlin protected modifier
// (protected void onCleared()).
actual abstract class ViewModel : ViewModel()
{
	actual val coroutineScope get() = viewModelScope
	
	actual override fun onCleared() { }
}
