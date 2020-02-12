package pl.karol202.sciorder.server.dao.impl

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.server.dao.ProductDao
import pl.karol202.sciorder.server.entity.ProductEntity

class ProductDaoImpl : ProductDao
{
	override suspend fun insertProduct(product: Product): Product
	{
		ProductEntity.new {
			store = product.
		}
	}
	
	override suspend fun updateProduct(product: Product)
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	override suspend fun deleteProduct(productId: Int)
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	override suspend fun getProductsByStore(storeId: Int): List<Product>
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	override suspend fun getProductById(productId: Int): Product?
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}
