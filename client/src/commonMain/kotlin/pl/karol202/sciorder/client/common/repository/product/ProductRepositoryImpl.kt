package pl.karol202.sciorder.client.common.repository.product

import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.ApiResponse.Error.Type.LOCAL_INCONSISTENCY
import pl.karol202.sciorder.client.common.api.product.ProductApi
import pl.karol202.sciorder.client.common.database.dao.ProductDao
import pl.karol202.sciorder.client.common.database.dao.delete
import pl.karol202.sciorder.client.common.database.dao.insert
import pl.karol202.sciorder.client.common.database.dao.update
import pl.karol202.sciorder.client.common.repository.resource.StandardMixedResource
import pl.karol202.sciorder.client.common.util.minutes
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductRequest

class ProductRepositoryImpl(private val productDao: ProductDao,
                            private val productApi: ProductApi) : ProductRepository
{
	override fun getProductsResource(token: String, storeId: Long) =
			StandardMixedResource(dao = productDao,
			                      databaseProvider = { getByStoreId(storeId) },
			                      apiProvider = { productApi.getProducts(token, storeId) },
			                      updateIntervalMillis = 5.minutes)
	
	override suspend fun addProduct(token: String, storeId: Long, product: ProductRequest): ApiResponse<Product>
	{
		suspend fun addLocally(product: Product) = productDao.insert(product)
		
		return productApi.addProduct(token, storeId, product).ifSuccess { addLocally(it) }
	}
	
	override suspend fun updateProduct(token: String, storeId: Long, productId: Long, product: ProductRequest): ApiResponse<Unit>
	{
		val previousProduct = productDao.getById(productId).first() ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)
		val updatedProduct = previousProduct.copy(name = product.name, available = product.available)
		
		suspend fun updateLocally() = productDao.update(updatedProduct)
		suspend fun revertLocally() = productDao.update(previousProduct)
		
		updateLocally()
		return productApi.updateProduct(token, storeId, productId, product).ifFailure { revertLocally() }
	}
	
	override suspend fun removeProduct(token: String, storeId: Long, productId: Long): ApiResponse<Unit>
	{
		val removedProduct = productDao.getById(productId).first() ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)
		
		suspend fun removeLocally() = productDao.delete(removedProduct)
		suspend fun revertLocally() = productDao.insert(removedProduct)
		
		removeLocally()
		return productApi.removeProduct(token, storeId, productId).ifFailure { revertLocally() }
	}
	
	override suspend fun cleanLocalProducts() = productDao.deleteAll()
}
