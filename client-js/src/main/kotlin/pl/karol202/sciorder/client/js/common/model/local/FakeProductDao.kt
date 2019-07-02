package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowViaChannel
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.js.common.model.local.FakeDao.IdUniqueElement
import pl.karol202.sciorder.client.js.common.util.Delegates
import pl.karol202.sciorder.client.js.common.util.invokeEach
import pl.karol202.sciorder.common.Product

class FakeProductDao : ProductDao, FakeDao
{
	private var updateListeners = listOf<(List<Product>) -> Unit>()
	private var products by Delegates.observable(setOf<IdUniqueElement<Product>>()) { updateListeners.invokeEach(it.values()) }

	override suspend fun insert(items: List<Product>)
	{
		products += items.wrap()
	}

	override suspend fun update(items: List<Product>)
	{
		products = products.update(items.wrap())
	}

	override suspend fun delete(items: List<Product>)
	{
		products -= items.wrap()
	}

	override suspend fun deleteAll()
	{
		products = emptySet()
	}

	override fun getAll() = flowViaChannel<List<Product>>(Channel.UNLIMITED) { channel ->
		val listener: (List<Product>) -> Unit = { channel.offer(it) }
		updateListeners += listener
		channel.invokeOnClose { updateListeners -= listener }
	}
}
