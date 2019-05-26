package pl.karol202.sciorder.client.common.repository.resource

import androidx.lifecycle.LiveData

interface Resource<T>
{
	val asLiveData: LiveData<ResourceState<T>>

	fun reload()
}
