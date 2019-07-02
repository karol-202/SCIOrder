package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowViaChannel
import pl.karol202.sciorder.client.common.model.local.OwnerDao
import pl.karol202.sciorder.client.js.common.util.Delegates
import pl.karol202.sciorder.common.Owner

class FakeOwnerDao : OwnerDao
{
	private var updateListeners = listOf<(Owner?) -> Unit>()
	private var owner by Delegates.observable<Owner?>(null, updateListeners)

	override suspend fun set(owner: Owner?)
	{
		this.owner = owner
	}

	override fun get() = flowViaChannel<Owner?>(Channel.UNLIMITED) { channel ->
		val listener: (Owner?) -> Unit = { channel.offer(it) }
		updateListeners += listener
		channel.invokeOnClose { updateListeners -= listener }
	}
}
