package pl.karol202.sciorder.client.common.model.local

import pl.karol202.sciorder.common.model.Owner

interface OwnerDao
{
	suspend fun set(owner: Owner?)

	suspend fun get(): Owner?
}
