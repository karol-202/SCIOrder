package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.js.common.model.local.FakeDao.IdUniqueElement
import pl.karol202.sciorder.client.js.common.util.invokeEach
import pl.karol202.sciorder.client.js.common.util.observable
import pl.karol202.sciorder.common.Product

class FakeProductDao : ProductDao, FakeDao
{
	private var updateListeners = listOf<(List<Product>) -> Unit>()
	private var products by observable(setOf<IdUniqueElement<Product>>()) { updateListeners.invokeEach(it.values()) }

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

	override fun getAll() = channelFlow<List<Product>> {
		send(products.values())

		val listener: (List<Product>) -> Unit = { offer(it) }
		updateListeners += listener
		awaitClose { updateListeners -= listener }
	}
}
