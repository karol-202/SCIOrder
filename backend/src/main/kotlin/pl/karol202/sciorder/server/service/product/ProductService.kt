package pl.karol202.sciorder.server.service.product

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.request.ProductCreateRequest
import pl.karol202.sciorder.common.request.ProductUpdateRequest

interface ProductService
{
	suspend fun insertProduct(storeId: Long, product: ProductCreateRequest): Product
	
	suspend fun updateProduct(storeId: Long, productId: Long, product: ProductUpdateRequest)
	
	suspend fun deleteProduct(storeId: Long, productId: Long)
	
	suspend fun getProducts(storeId: Long): List<Product>
	
	suspend fun getProduct(storeId: Long, productId: Long): Product?
}
