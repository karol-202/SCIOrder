package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import pl.karol202.sciorder.client.common.model.local.OwnerDao
import pl.karol202.sciorder.client.js.common.util.Storage
import pl.karol202.sciorder.client.js.common.util.invokeEach
import pl.karol202.sciorder.client.js.common.util.observable
import pl.karol202.sciorder.common.Owner

class FakeOwnerDao : OwnerDao
{
	private var updateListeners = listOf<(Owner?) -> Unit>()
	private var owner by observable<Owner?>(null) { updateListeners.invokeEach(it) }

	override suspend fun set(owner: Owner?)
	{
		this.owner = owner
		saveOwner(owner)
	}

	private fun saveOwner(owner: Owner?)
	{
		Storage[Storage.OWNER_ID] = owner?.id
		Storage[Storage.OWNER_NAME] = owner?.name
		Storage[Storage.OWNER_HASH] = owner?.hash
	}

	override fun get() = channelFlow<Owner?> {
		send(loadOwner())

		val listener: (Owner?) -> Unit = { offer(it) }
		updateListeners += listener
		awaitClose { updateListeners -= listener }
	}

	private fun loadOwner(): Owner?
	{
		val id = Storage[Storage.OWNER_ID]
		val name = Storage[Storage.OWNER_NAME]
		val hash = Storage[Storage.OWNER_HASH]
		return Owner(id ?: return null, name ?: return null, hash ?: "")
	}
}
