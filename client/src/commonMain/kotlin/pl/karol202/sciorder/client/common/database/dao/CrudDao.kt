package pl.karol202.sciorder.client.common.database.dao

import pl.karol202.sciorder.common.util.IdProvider
import pl.karol202.sciorder.common.util.ids

interface CrudDao<T>
{
	suspend fun insert(items: List<T>)

	suspend fun update(items: List<T>)

	suspend fun delete(items: List<T>)
}

suspend fun <T> CrudDao<T>.insert(item: T) = insert(listOf(item))

suspend fun <T> CrudDao<T>.update(item: T) = update(listOf(item))

suspend fun <T> CrudDao<T>.delete(item: T) = delete(listOf(item))

suspend fun <T : IdProvider> CrudDao<T>.dispatchDiff(oldData: List<T>, newData: List<T>)
{
	insert(newData.filterNot { it.id in oldData.ids })
	update(newData.filter { it.id in oldData.ids && it !in oldData })
	delete(oldData.filterNot { it.id in newData.ids })
}
