package pl.karol202.sciorder.client.android.common.repository.owner

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.extensions.doOnSuccess
import pl.karol202.sciorder.client.common.model.remote.OwnerApi
import pl.karol202.sciorder.common.model.Owner

class OwnerRepositoryImpl(private val coroutineScope: CoroutineScope,
                          private val ownerDao: OwnerDao,
                          private val ownerApi: OwnerApi) : OwnerRepository
{
	override fun getOwner() = ownerDao.get()

	override fun login(name: String, hash: String?) = ownerApi.getOwnerByName(name, hash).saveOnSuccess()

	override fun register(name: String, hash: String) = ownerApi.addOwner(Owner.create(name, hash)).saveOnSuccess()

	private fun LiveData<ApiResponse<Owner>>.saveOnSuccess() = doOnSuccess { saveLocally(it) }

	override fun logout() = saveLocally(null)

	private fun saveLocally(owner: Owner?)
	{
		coroutineScope.launch { ownerDao.set(owner) }
	}
}
