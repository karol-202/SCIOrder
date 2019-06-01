package pl.karol202.sciorder.client.android.common.model.local.owner

import pl.karol202.sciorder.client.android.common.extensions.map
import pl.karol202.sciorder.common.model.Owner

fun OwnerEntityDao.toOwnerDao(): OwnerDao = OwnerDaoImpl(this)

class OwnerDaoImpl(private val ownerEntityDao: OwnerEntityDao) : OwnerDao
{
	override suspend fun set(owner: Owner?) =
			if(owner != null) ownerEntityDao.set(owner.toOwnerEntity())
			else ownerEntityDao.delete()

	override fun get() = ownerEntityDao.get().map { it?.toOwner() }

	private fun OwnerEntity.toOwner() = Owner(id, name, hash)

	private fun Owner.toOwnerEntity() = OwnerEntity(id, name, hash)
}
