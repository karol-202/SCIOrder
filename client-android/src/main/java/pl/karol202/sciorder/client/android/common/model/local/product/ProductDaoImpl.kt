package pl.karol202.sciorder.client.android.common.model.local.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.android.common.extensions.map
import pl.karol202.sciorder.common.model.Product

fun ProductEntityDao.toProductDao(): ProductDao = ProductDaoImpl(this)

private class ProductDaoImpl(private val productEntityDao: ProductEntityDao) : ProductDao
{
	override suspend fun insert(items: List<Product>) = productEntityDao.insert(items.map { it.toProductEntity() })

	override suspend fun update(items: List<Product>) = productEntityDao.update(items.map { it.toProductEntity() })

	override suspend fun delete(items: List<Product>) = productEntityDao.delete(items.map { it.toProductEntity() })

	override suspend fun deleteAll() = productEntityDao.deleteAll()

	override fun getAll(): LiveData<List<Product>> =
			productEntityDao.getAll().map { entities -> entities.map { it.toProduct() } }

	private fun ProductEntity.toProduct() = Product(id, "", name, available, parameters)

	private fun Product.toProductEntity() = ProductEntity(id, name, available, parameters)
}
