package pl.karol202.sciorder.client.js.common.model.local

import kotlinx.coroutines.flow.map
import kotlinx.serialization.set
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.js.common.model.local.FakeDao.IdUniqueElement
import pl.karol202.sciorder.common.Product

class LocalProductDao(storageId: String) :
		LocalDao<Set<IdUniqueElement<Product>>>(storageId,
		                                        IdUniqueElement.serializer(Product.serializer()).set,
		                                        emptySet()),
		ProductDao,
		FakeDao
{
	override suspend fun insert(items: List<Product>) = setData { it + items.wrap() }

	override suspend fun update(items: List<Product>) = setData { it.update(items.wrap()) }

	override suspend fun delete(items: List<Product>) = setData { it - items.wrap() }

	override suspend fun deleteAll() = setData { emptySet() }

	override fun getAll() = getFromStorage().map { it.values() }
	
	override fun getById(id: String) = getAll().map { products ->
		products.singleOrNull { it.id == id }
	}
}
