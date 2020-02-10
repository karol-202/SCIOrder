package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow

interface OwnerDao
{
	suspend fun set(owner: Owner?)

	fun get(): Flow<Owner?>
}
