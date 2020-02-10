package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.Owner

interface OwnerDao
{
	suspend fun set(owner: Owner?)

	fun get(): Flow<Owner?>
}
