package pl.karol202.sciorder.client.common.model.local.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.karol202.sciorder.common.model.Product

fun ProductEntityDao.toProductDao(): ProductDao = ProductDaoImpl(this)

private class ProductDaoImpl(private val productEntityDao: ProductEntityDao) : ProductDao
{
	@WorkerThread
	override fun insert(items: List<Product>) = productEntityDao.insert(items.map { it.toProductEntity() })

	@WorkerThread
	override fun update(items: List<Product>) = productEntityDao.update(items.map { it.toProductEntity() })

	@WorkerThread
	override fun delete(items: List<Product>) = productEntityDao.delete(items.map { it.toProductEntity() })

	override fun getAll(): LiveData<List<Product>> =
			Transformations.map(productEntityDao.getAll()) { entities -> entities.map { it.toProduct() } }

	private fun ProductEntity.toProduct() = Product(id, name, available, parameters)

	private fun Product.toProductEntity() = ProductEntity(_id, name, available, parameters)
}
