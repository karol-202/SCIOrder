package pl.karol202.sciorder.client.common.model.local.owner

import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.common.extensions.asFlowWrapped
import pl.karol202.sciorder.client.common.model.local.OwnerDao
import pl.karol202.sciorder.common.Owner

fun OwnerEntityDao.toOwnerDao(): OwnerDao = RoomOwnerDao(this)

class RoomOwnerDao(private val ownerEntityDao: OwnerEntityDao) : OwnerDao
{
	override suspend fun set(owner: Owner?) =
			if(owner != null) ownerEntityDao.set(owner.toOwnerEntity())
			else ownerEntityDao.delete()

	override fun get() = ownerEntityDao.get().asFlowWrapped().map { it?.toOwner() }

	private fun OwnerEntity.toOwner() = Owner(id, name, hash)

	private fun Owner.toOwnerEntity() = OwnerEntity(id, name, hash)
}
