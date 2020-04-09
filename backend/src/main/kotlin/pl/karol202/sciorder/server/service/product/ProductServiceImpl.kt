package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductParameterRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest
import pl.karol202.sciorder.server.controller.notFound
import pl.karol202.sciorder.server.entity.ProductEntity
import pl.karol202.sciorder.server.entity.StoreEntity
import pl.karol202.sciorder.server.service.product.parameter.ProductParameterService
import pl.karol202.sciorder.server.table.Products
import pl.karol202.sciorder.server.util.map

class ProductServiceImpl(private val productParameterService: ProductParameterService) : ProductService
{
	override suspend fun insertProduct(storeId: Long, product: ProductCreateRequest): Product
	{
		val storeEntity = StoreEntity.findById(storeId) ?: notFound()
		val productEntity = ProductEntity.new {
			store = storeEntity
			name = product.name
			available = product.available
		}
		product.createdParameters.forEach { insertParameter(storeId, productEntity.id.value, it) }
		return productEntity.map()
	}
	
	override suspend fun updateProduct(storeId: Long, productId: Long, product: ProductUpdateRequest): Product
	{
		val productEntity = ProductEntity.findById(productId)?.takeIf(storeId = storeId) ?: notFound()
		productEntity.name = product.name
		productEntity.available = product.available
		product.createdParameters.forEach { insertParameter(storeId, productEntity.id.value, it) }
		product.updatedParameters.forEach { (id, param) -> updateParameter(storeId, productEntity.id.value, id, param) }
		product.removedParameters.forEach { deleteParameter(storeId, productEntity.id.value, it) }
		return productEntity.map()
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
	
	private suspend fun insertParameter(storeId: Long, productId: Long, parameter: ProductParameterRequest) =
			productParameterService.insertParameter(storeId, productId, parameter)
	
	private suspend fun updateParameter(storeId: Long, productId: Long, parameterId: Long, parameter: ProductParameterRequest) =
			productParameterService.updateParameter(storeId, productId, parameterId, parameter)
	
	private suspend fun deleteParameter(storeId: Long, productId: Long, parameterId: Long) =
			productParameterService.deleteParameter(storeId, productId, parameterId)
}
