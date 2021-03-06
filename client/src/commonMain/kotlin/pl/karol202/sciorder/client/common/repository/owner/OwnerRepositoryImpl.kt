package pl.karol202.sciorder.client.common.repository.owner

import pl.karol202.sciorder.client.common.model.local.OwnerDao
import pl.karol202.sciorder.client.common.model.remote.ApiResponse
import pl.karol202.sciorder.client.common.model.remote.owner.OwnerApi
import pl.karol202.sciorder.common.Owner

class OwnerRepositoryImpl(private val ownerDao: OwnerDao,
                          private val ownerApi: OwnerApi) : OwnerRepository
{
	override fun getOwnerFlow() = ownerDao.get()

	override suspend fun login(name: String, hash: String?) = ownerApi.getOwnerByName(name, hash).saveIfSuccess()

	override suspend fun register(owner: Owner) = ownerApi.addOwner(owner).saveIfSuccess()

	private suspend fun ApiResponse<Owner>.saveIfSuccess() = ifSuccess { ownerDao.set(it) }

	override suspend fun logout() = ownerDao.set(null)
}
