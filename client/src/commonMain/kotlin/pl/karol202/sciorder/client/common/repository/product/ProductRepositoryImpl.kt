package pl.karol202.sciorder.client.common.repository.product

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import pl.karol202.sciorder.client.common.api.ApiResponse
import pl.karol202.sciorder.client.common.api.ApiResponse.Error.Type.LOCAL_INCONSISTENCY
import pl.karol202.sciorder.client.common.api.product.ProductApi
import pl.karol202.sciorder.client.common.database.dao.ProductDao
import pl.karol202.sciorder.client.common.database.dao.delete
import pl.karol202.sciorder.client.common.database.dao.insert
import pl.karol202.sciorder.client.common.database.dao.update
import pl.karol202.sciorder.client.common.repository.resource.DaoMixedResource
import pl.karol202.sciorder.client.common.util.minutes
import pl.karol202.sciorder.common.model.Owner
import pl.karol202.sciorder.common.model.Product

class ProductRepositoryImpl(private val productDao: ProductDao,
                            private val productApi: ProductApi) : ProductRepository
{
	override fun getProductsResource(ownerId: String) = object : DaoMixedResource<Product>(productDao)
	{
		override suspend fun waitForNextUpdate() = delay(5.minutes)
		
		override suspend fun loadFromNetwork(oldData: List<Product>) = productApi.getAllProducts(ownerId)
	}
	
	override suspend fun addProduct(owner: Owner, product: Product): ApiResponse<Product>
	{
		suspend fun addLocally(product: Product) = productDao.insert(product)
		suspend fun revertLocally(product: Product) = productDao.delete(product)
		suspend fun patchLocally(patched: Product)
		{
			revertLocally(product)
			addLocally(patched)
		}
		
		addLocally(product)
		return productApi.addProduct(owner.id, owner.hash, product).ifSuccess { patchLocally(it) }
																   .ifFailure { revertLocally(product) }
	}
	
	override suspend fun updateProduct(owner: Owner, product: Product): ApiResponse<Unit>
	{
		val previousProduct = productDao.getById(product.id).first() ?: return ApiResponse.Error(LOCAL_INCONSISTENCY)
		
		suspend fun updateLocally() = productDao.update(product)
		suspend fun revertLocally() = productDao.update(previousProduct)
		
		updateLocally()
		return productApi.updateProduct(owner.id, product.id, owner.hash, product).ifFailure { revertLocally() }
	}
	
	override suspend fun removeProduct(owner: Owner, product: Product): ApiResponse<Unit>
	{
		suspend fun removeLocally() = productDao.delete(product)
		suspend fun revertLocally() = productDao.insert(product)
		
		removeLocally()
		return productApi.removeProduct(owner.id, product.id, owner.hash).ifFailure { revertLocally() }
	}
}
