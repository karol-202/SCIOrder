package pl.karol202.sciorder.client.android.common.repository.resource

import androidx.lifecycle.LiveData

class EmptyResource<T> : Resource<T>
{
	override val asLiveData = object : LiveData<ResourceState<T>>() { }

	override fun reload() { }
}
