package pl.karol202.sciorder.client.common.model.local

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.Owner

interface OwnerDao
{
	suspend fun set(owner: Owner?)

	fun get(): Flow<Owner?>
}
