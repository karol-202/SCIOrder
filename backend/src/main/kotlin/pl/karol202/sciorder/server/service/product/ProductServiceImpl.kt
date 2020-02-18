package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductRequest
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.ProductEntity
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.table.Products
import pl.karol202.sciorder.server.util.map

class ProductServiceImpl : ProductService
{
	override suspend fun insertProduct(storeId: Long, product: ProductRequest): Product
	{
		val storeEntity = StoreEntity.findById(storeId) ?: notFound()
		val productEntity = ProductEntity.new {
			store = storeEntity
			name = product.name
			available = product.available
		}
		return productEntity.map()
	}
	
	override suspend fun updateProduct(storeId: Long, productId: Long, product: ProductRequest)
	{
		val productEntity = ProductEntity.findById(productId)?.takeIf(storeId = storeId) ?: notFound()
		productEntity.name = product.name
		productEntity.available = product.available
	}
	
	override suspend fun deleteProduct(storeId: Long, productId: Long)
	{
		val productEntity = ProductEntity.findById(productId)?.takeIf(storeId = storeId) ?: notFound()
		productEntity.delete()
	}
	
	override suspend fun getProducts(storeId: Long): List<Product>
	{
		StoreEntity.findById(storeId) ?: notFound()
		return ProductEntity.find { Products.storeId eq storeId }.map()
	}
	
	override suspend fun getProduct(storeId: Long, productId: Long): Product?
	{
		StoreEntity.findById(storeId) ?: notFound()
		return ProductEntity.findById(productId)?.map()
	}
	
	private fun ProductEntity.takeIf(storeId: Long) = takeIf { it.store.id.value == storeId }
}
