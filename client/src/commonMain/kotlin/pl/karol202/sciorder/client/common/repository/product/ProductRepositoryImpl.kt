package pl.karol202.sciorder.client.common.repository.product

import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.ApiResponse.Error.Type.LOCAL_INCONSISTENCY
import pl.karol202.sciorder.client.common.api.product.ProductApi
import pl.karol202.sciorder.client.common.database.dao.ProductDao
import pl.karol202.sciorder.client.common.database.dao.delete
import pl.karol202.sciorder.client.common.database.dao.insert
import pl.karol202.sciorder.client.common.database.dao.update
import pl.karol202.sciorder.client.common.repository.resource.StandardResource
import pl.karol202.sciorder.client.common.util.minutes
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest

class ProductRepositoryImpl(private val productDao: ProductDao,
                            private val productApi: ProductApi) : ProductRepository
{
	override fun getProductsResource(token: String, storeId: Long) =
			StandardResource(updateIntervalMillis = 5.minutes,
			                 getFromApi = { productApi.getProducts(token, storeId) },
			                 getFromDB = { productDao.getByStoreId(storeId) },
			                 saveToDB = { productDao.dispatchByStoreId(storeId, it) })
	
	override suspend fun addProduct(token: String, storeId: Long, product: ProductCreateRequest): ApiResponse<Product>
	{
		suspend fun addLocally(product: Product) = productDao.insert(product)
		
		return productApi.addProduct(token, storeId, product).ifSuccess { addLocally(it) }
	}
	
	override suspend fun updateProduct(token: String, storeId: Long, productId: Long, product: ProductUpdateRequest):
			ApiResponse<Unit>
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
