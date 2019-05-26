package pl.karol202.sciorder.client.common.repository.resource

import androidx.lifecycle.LiveData

class EmptyResource<T> : Resource<T>
{
	override val asLiveData = object : LiveData<ResourceState<T>>() { }

	override fun reload() { }
}
