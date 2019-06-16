package pl.karol202.sciorder.client.common.repository.product

import pl.karol202.sciorder.client.common.extensions.TimeUnit
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.client.common.model.remote.ProductApi
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.client.common.repository.resource.UpdateTimeout
import pl.karol202.sciorder.common.Owner
import pl.karol202.sciorder.common.Product

class ProductRepositoryImpl(private val productDao: ProductDao,
                            private val productApi: ProductApi) : ProductRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.MINUTES)

	override fun getAllProducts(ownerId: String) = object : DaoMixedResource<Product>(productDao) {
		override fun shouldFetchFromNetwork(data: List<Product>) = data.isEmpty() || updateTimeout.shouldUpdate()

		override suspend fun loadFromNetwork(oldData: List<Product>) = productApi.getAllProducts(ownerId)
	}

	override suspend fun addProduct(owner: Owner, product: Product) =
			productApi.addProduct(owner.id, owner.hash, product).ifSuccess { addProductLocally(it) }

	override suspend fun updateProduct(owner: Owner, product: Product) =
			productApi.updateProduct(owner.id, product.id, owner.hash, product).ifSuccess { updateProductLocally(product) }

	override suspend fun removeProduct(owner: Owner, product: Product) =
			productApi.removeProduct(owner.id, product.id, owner.hash).ifSuccess { removeProductLocally(product) }

	private suspend fun addProductLocally(product: Product) = productDao.insert(listOf(product))

	private suspend fun updateProductLocally(product: Product) = productDao.update(listOf(product))

	private suspend fun removeProductLocally(product: Product) = productDao.delete(listOf(product))
}
