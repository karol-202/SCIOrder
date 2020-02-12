package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.entity.ProductEntity
import pl.karol202.sciorder.server.util.toModels

interface ProductServiceImpl : ProductService
{
	override suspend fun getProducts(storeId: Long): List<Product>
	{
		return ProductEntity.all().toModels()
	}
}
