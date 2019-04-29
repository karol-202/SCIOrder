package pl.karol202.sciorder.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.model.local.product.ProductDao
import pl.karol202.sciorder.model.remote.product.ProductApi
import java.util.concurrent.TimeUnit

class ProductRepositoryImpl(private val coroutineScope: CoroutineScope,
                            private val productDao: ProductDao,
                            private val productApi: ProductApi): ProductRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.MINUTES)

	override fun getAllProducts() = object : Resource<List<Product>>() {
		override fun shouldFetchFromNetwork(data: List<Product>) = data.isEmpty() || updateTimeout.shouldUpdate()

		override fun loadFromDatabase() = productDao.getAllProducts()

		override fun loadFromNetwork() = productApi.getAllProducts()

		override fun saveToDatabase(data: List<Product>)
		{
			// It may be required for Dao not to be called on main thread
			coroutineScope.launch {
				productDao.clearProducts()
				productDao.insertProducts(data)
			}
		}
	}
}