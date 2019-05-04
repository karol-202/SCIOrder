package pl.karol202.sciorder.client.common.model.local.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.karol202.sciorder.common.model.Product

fun ProductEntityDao.toProductDao(): ProductDao = ProductDaoImpl(this)

private class ProductDaoImpl(private val productEntityDao: ProductEntityDao) : ProductDao
{
	@WorkerThread
	override fun insertProducts(products: List<Product>) =
		productEntityDao.insertProducts(products.map { it.toProductEntity() })

	@WorkerThread
	override fun deleteProducts() = productEntityDao.deleteProducts()

	override fun getAllProducts(): LiveData<List<Product>> =
			Transformations.map(productEntityDao.getAllProducts()) { entities -> entities.map { it.toProduct() } }

	private fun ProductEntity.toProduct() = Product(id, name, available, parameters)

	private fun Product.toProductEntity() = ProductEntity(_id, name, available, parameters)
}
