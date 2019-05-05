package pl.karol202.sciorder.client.common.repository.product

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.karol202.sciorder.client.common.extensions.observeOnceNonNull
import pl.karol202.sciorder.client.common.model.local.dispatchDiff
import pl.karol202.sciorder.client.common.model.local.product.ProductDao
import pl.karol202.sciorder.client.common.model.remote.product.ProductApi
import pl.karol202.sciorder.client.common.repository.Resource
import pl.karol202.sciorder.client.common.repository.RoomResource
import pl.karol202.sciorder.client.common.repository.UpdateTimeout
import pl.karol202.sciorder.common.model.Product
import java.util.concurrent.TimeUnit

class ProductRepositoryImpl(private val coroutineScope: CoroutineScope,
                            private val productDao: ProductDao,
                            private val productApi: ProductApi): ProductRepository
{
	private val updateTimeout = UpdateTimeout(10, TimeUnit.MINUTES)

	override fun getAllProducts() = object : RoomResource<Product>(coroutineScope, productDao) {
		override fun shouldFetchFromNetwork(data: List<Product>) = data.isEmpty() || updateTimeout.shouldUpdate()

		override fun loadFromNetwork(oldData: List<Product>) = productApi.getAllProducts()
	}
}
