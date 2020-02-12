package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.model.Product

interface ProductService
{
	suspend fun insertProduct(product: Product)
	
	suspend fun getProducts(storeId: Long): List<Product>
}
