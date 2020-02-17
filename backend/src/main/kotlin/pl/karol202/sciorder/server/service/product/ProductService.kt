package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.model.Product

interface ProductService
{
	suspend fun insertProduct(storeId: Long, product: Product): Product
	
	suspend fun putProduct(storeId: Long, productId: Long, product: Product)
	
	suspend fun deleteProduct(storeId: Long, productId: Long)
	
	suspend fun getProducts(storeId: Long): List<Product>
}
