package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.model.local.CrudDao
import pl.karol202.sciorder.client.common.model.local.dispatchDiff
import pl.karol202.sciorder.common.IdProvider

abstract class DaoMixedResource<T : IdProvider>(private val dao: CrudDao<T>,
                                                databaseSource: Flow<List<T>> = dao.getAll()) :
		MixedResource<List<T>>(databaseSource)
{
	override suspend fun saveToDatabase(data: List<T>) = dao.dispatchDiff(databaseSource.first(), data)
}
