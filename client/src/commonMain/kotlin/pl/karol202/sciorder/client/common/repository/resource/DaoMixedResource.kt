package pl.karol202.sciorder.client.common.repository.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.database.dao.CrudDao
import pl.karol202.sciorder.client.common.database.dispatchDiff
import pl.karol202.sciorder.common.util.IdProvider

abstract class DaoMixedResource<T : IdProvider>(private val dao: CrudDao<T>,
                                                                                                    databaseFlow: Flow<List<T>> = dao.getAll()) :
		MixedResource<List<T>>(databaseFlow)
{
	override suspend fun saveToDatabase(data: List<T>) = dao.dispatchDiff(databaseFlow.first(), data)
}
