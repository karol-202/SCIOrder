package pl.karol202.sciorder.client.common.repository.product

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.client.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.common.model.remote.product.ProductApi
import pl.karol202.sciorder.client.common.repository.Resource
import pl.karol202.sciorder.client.common.repository.UpdateTimeout
import java.util.concurrent.TimeUnit

class ProductRepositoryImpl(private val coroutineScope: CoroutineScope,
                            private val productDao: ProductDao,
                            private val productApi: ProductApi): ProductRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.MINUTES)

	override fun getAllProducts() = object : Resource<List<Product>>() {
		override fun shouldFetchFromNetwork(data: List<Product>) = data.isEmpty() || updateTimeout.shouldUpdate()

		override fun loadFromDatabase() = productDao.getAllProducts()

		override fun loadFromNetwork(oldData: List<Product>) = productApi.getAllProducts()

		override fun saveToDatabase(data: List<Product>)
		{
			// It may be required for Dao not to be called on main thread
			coroutineScope.launch {
				productDao.deleteProducts()
				productDao.insertProducts(data)
			}
		}
	}
}
