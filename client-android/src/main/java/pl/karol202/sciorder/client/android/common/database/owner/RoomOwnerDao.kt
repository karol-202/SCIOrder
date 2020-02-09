package pl.karol202.sciorder.client.android.common.database.owner

import kotlinx.coroutines.reactive.flow.asFlow
import pl.karol202.sciorder.client.common.database.owner.OwnerDao
import pl.karol202.sciorder.common.model.Owner

fun OwnerEntityDao.toOwnerDao(): OwnerDao = RoomOwnerDao(this)

class RoomOwnerDao(private val ownerEntityDao: OwnerEntityDao) : OwnerDao
{
	companion object
	{
		private val ABSENT_OWNER_ENTITY = OwnerEntity(null, "", "")
	}

	override suspend fun set(owner: Owner?) = ownerEntityDao.set(owner?.toOwnerEntity() ?: ABSENT_OWNER_ENTITY)

	override fun get() = ownerEntityDao.get().asFlow().map { it.toOwner() }

	private fun OwnerEntity.toOwner() = id?.let { Owner(it, name, hash) }

	private fun Owner.toOwnerEntity() = OwnerEntity(id, name, hash)
}
