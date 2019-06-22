package pl.karol202.sciorder.client.android.common.model.local.product

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.flow.asFlow
import pl.karol202.sciorder.client.common.model.local.ProductDao
import pl.karol202.sciorder.common.Product

fun ProductEntityDao.toProductDao(): ProductDao = RoomProductDao(this)

private class RoomProductDao(private val productEntityDao: ProductEntityDao) : ProductDao
{
	override suspend fun insert(items: List<Product>) = productEntityDao.insert(items.map { it.toProductEntity() })

	override suspend fun update(items: List<Product>) = productEntityDao.update(items.map { it.toProductEntity() })

	override suspend fun delete(items: List<Product>) = productEntityDao.delete(items.map { it.toProductEntity() })

	override suspend fun deleteAll() = productEntityDao.deleteAll()

	override fun getAll() = productEntityDao.getAll().asFlow().map { entities -> entities.map { it.toProduct() } }

	private fun ProductEntity.toProduct() = Product(id, "", name, available, parameters)

	private fun Product.toProductEntity() = ProductEntity(id, name, available, parameters)
}
