package pl.karol202.sciorder.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.model.local.ProductDao
import pl.karol202.sciorder.model.remote.ProductApi

class ProductRepository(private val coroutineScope: CoroutineScope,
                        private val productDao: ProductDao,
                        private val productApi: ProductApi)
{
	fun getAllProducts() = object : Resource<List<Product>>() {
		override fun shouldFetchFromNetwork(data: List<Product>) = data.isEmpty()

		override fun loadFromDatabase() = productDao.getAllProducts()

		override fun loadFromNetwork() = productApi.getAllProducts()

		override fun saveToDatabase(data: List<Product>)
		{
			coroutineScope.launch { productDao.insertProducts(data) }
		}
	}
}