package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.controller.StatusException
import pl.karol202.sciorder.server.entity.ProductEntity
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.entity.mapping.map
import pl.karol202.sciorder.server.table.Products

interface ProductServiceImpl : ProductService
{
	override suspend fun insertProduct(storeId: Long, product: Product): Product
	{
		val storeEntity = StoreEntity.findById(storeId) ?: throw StatusException.NotFound()
		val productEntity = ProductEntity.new {
			store = storeEntity
			update(product)
		}
		return productEntity.map()
	}
	
	override suspend fun putProduct(storeId: Long, productId: Long, product: Product)
	{
		val productEntity = ProductEntity.findById(productId)?.takeIf { it.store.id.value == storeId }
		productEntity ?: throw StatusException.NotFound()
		productEntity.update(product)
	}
	
	override suspend fun deleteProduct(storeId: Long, productId: Long)
	{
		val productEntity = ProductEntity.findById(productId)?.takeIf { it.store.id.value == storeId }
		productEntity ?: throw StatusException.NotFound()
		productEntity.delete()
	}
	
	override suspend fun getProducts(storeId: Long) = ProductEntity.find { Products.storeId eq storeId }.map()
}
