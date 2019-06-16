package pl.karol202.sciorder.client.common.model.local

import kotlinx.coroutines.flow.Flow

interface CrudDao<T>
{
	suspend fun insert(items: List<T>)

	suspend fun update(items: List<T>)

	suspend fun delete(items: List<T>)

	fun getAll(): Flow<List<T>>
}
