package pl.karol202.sciorder.client.android.common.model.local.owner

import pl.karol202.sciorder.client.common.model.local.OwnerDao
import pl.karol202.sciorder.common.model.Owner

fun OwnerEntityDao.toOwnerDao(): OwnerDao = RoomOwnerDao(this)

class RoomOwnerDao(private val ownerEntityDao: OwnerEntityDao) : OwnerDao
{
	override suspend fun set(owner: Owner?) =
			if(owner != null) ownerEntityDao.set(owner.toOwnerEntity())
			else ownerEntityDao.delete()

	override suspend fun get() = ownerEntityDao.get()?.toOwner()

	private fun OwnerEntity.toOwner() = Owner(id, name, hash)

	private fun Owner.toOwnerEntity() = OwnerEntity(id, name, hash)
}
