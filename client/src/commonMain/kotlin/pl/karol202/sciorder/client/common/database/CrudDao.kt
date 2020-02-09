package pl.karol202.sciorder.client.common.database

import kotlinx.coroutines.flow.Flow

interface CrudDao<T>
{
	suspend fun insert(items: List<T>)

	suspend fun update(items: List<T>)

	suspend fun delete(items: List<T>)

	fun getAll(): Flow<List<T>>
}

suspend fun <T> CrudDao<T>.insert(item: T) = insert(listOf(item))

suspend fun <T> CrudDao<T>.update(item: T) = update(listOf(item))

suspend fun <T> CrudDao<T>.delete(item: T) = delete(listOf(item))
