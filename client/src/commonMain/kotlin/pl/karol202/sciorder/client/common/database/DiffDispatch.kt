package pl.karol202.sciorder.client.common.database

import pl.karol202.sciorder.client.common.database.dao.CrudDao
import pl.karol202.sciorder.common.model.IdProvider

suspend fun <T : IdProvider> CrudDao<T>.dispatchDiff(oldData: List<T>, newData: List<T>)
{
	insert(newData.filterNot { it.id in oldData.ids })
	update(newData.filter { it.id in oldData.ids && it !in oldData })
	delete(oldData.filterNot { it.id in newData.ids })
}

private val <T : IdProvider> List<T>.ids get() = map { it.id }
