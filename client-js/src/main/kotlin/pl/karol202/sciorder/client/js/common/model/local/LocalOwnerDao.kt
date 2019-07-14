package pl.karol202.sciorder.client.js.common.model.local

import pl.karol202.sciorder.client.common.model.local.OwnerDao
import pl.karol202.sciorder.common.Owner

class LocalOwnerDao : NullableLocalDao<Owner>(LocalStorage.STORAGE_OWNER, Owner.serializer(), null), OwnerDao
{
	override suspend fun set(owner: Owner?) = setNullableData { owner }

	override fun get() = getNullableFromStorage()
}
