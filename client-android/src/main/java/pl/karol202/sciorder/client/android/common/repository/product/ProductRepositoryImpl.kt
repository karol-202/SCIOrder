package pl.karol202.sciorder.client.android.common.repository.product

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.android.common.repository.resource.RoomResource
import pl.karol202.sciorder.client.android.common.repository.resource.UpdateTimeout
import pl.karol202.sciorder.client.common.model.remote.ProductApi
import pl.karol202.sciorder.common.model.Owner
import pl.karol202.sciorder.common.model.Product
import java.util.concurrent.TimeUnit

class ProductRepositoryImpl(private val coroutineScope: CoroutineScope,
                            private val productDao: ProductDao,
                            private val productApi: ProductApi) : ProductRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.MINUTES)

	override fun getAllProducts(ownerId: String) = object : RoomResource<Product>(coroutineScope, productDao) {
		override fun shouldFetchFromNetwork(data: List<Product>) = data.isEmpty() || updateTimeout.shouldUpdate()

		override fun loadFromNetwork(oldData: List<Product>) = productApi.getAllProducts(ownerId)
	}

	override fun addProduct(owner: Owner, product: Product) =
			productApi.addProduct(owner.id, owner.hash, product).doOnSuccess { addProductLocally(it) }

	private fun addProductLocally(product: Product) = coroutineScope.launch { productDao.insert(listOf(product)) }

	override fun updateProduct(owner: Owner, product: Product) =
			productApi.updateProduct(owner.id, product.id, owner.hash, product).doOnSuccess { updateProductLocally(product) }

	private fun updateProductLocally(product: Product) = coroutineScope.launch { productDao.update(listOf(product)) }

	override fun removeProduct(owner: Owner, product: Product) =
			productApi.removeProduct(owner.id, product.id, owner.hash).doOnSuccess { removeProductLocally(product) }

	private fun removeProductLocally(product: Product) = coroutineScope.launch { productDao.delete(listOf(product)) }
}
