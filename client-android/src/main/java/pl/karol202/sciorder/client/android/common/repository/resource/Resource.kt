package pl.karol202.sciorder.client.android.common.repository.resource

import androidx.lifecycle.LiveData

interface Resource<T>
{
	val asLiveData: LiveData<ResourceState<T>>

	fun reload()
}
