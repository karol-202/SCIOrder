package pl.karol202.sciorder.model.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.karol202.sciorder.model.Product

fun ProductEntityDao.toProductDao(): ProductDao = ProductDaoImpl(this)

private class ProductDaoImpl(private val productEntityDao: ProductEntityDao) : ProductDao
{
	override fun insertProducts(products: List<Product>)
	{
		productEntityDao.insertProducts(products.map(::toProductEntity))
	}

	override fun getAllProducts(): LiveData<List<Product>> =
			Transformations.map(productEntityDao.getAllProducts()) { it.map(::fromProductEntity) }

	private fun fromProductEntity(productEntity: ProductEntity) =
			Product(productEntity.id,
					productEntity.name,
					productEntity.available,
					productEntity.parameters)

	private fun toProductEntity(product: Product) =
			ProductEntity(product._id, product.name, product.available, product.parameters)
}