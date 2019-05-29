package pl.karol202.sciorder.client.common.model.local

import androidx.lifecycle.LiveData

interface CrudDao<T>
{
	suspend fun insert(items: List<T>)

	suspend fun update(items: List<T>)

	suspend fun delete(items: List<T>)

	fun getAll(): LiveData<List<T>>
}
