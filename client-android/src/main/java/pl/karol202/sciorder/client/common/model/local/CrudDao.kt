package pl.karol202.sciorder.client.common.model.local

import androidx.lifecycle.LiveData

interface CrudDao<T>
{
	fun insert(items: List<T>)

	fun update(items: List<T>)

	fun delete(items: List<T>)

	fun getAll(): LiveData<List<T>>
}
