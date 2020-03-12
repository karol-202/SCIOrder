package pl.karol202.sciorder.client.android.common.database.daoimpl

import kotlinx.coroutines.reactive.flow.asFlow
import pl.karol202.sciorder.client.android.common.database.dao.ProductEntityDao
import pl.karol202.sciorder.client.android.common.database.entity.ProductEntity
import pl.karol202.sciorder.client.common.database.dao.ProductDao
import pl.karol202.sciorder.common.model.Product

fun ProductEntityDao.toProductDao(): ProductDao = RoomProductDao(this)

private class RoomProductDao(private val productEntityDao: ProductEntityDao) : ProductDao
{
	override suspend fun insert(items: List<Product>) = productEntityDao.insert(items.toProductEntities())

	override suspend fun update(items: List<Product>) = productEntityDao.update(items.toProductEntities())

	override suspend fun delete(items: List<Product>) = productEntityDao.delete(items.toProductEntities())

	override suspend fun deleteAll() = productEntityDao.deleteAll()

	override fun getAll() = productEntityDao.getAll().asFlow().map { it.toProducts() }
	
	override fun getById(productId: String) = productEntityDao.getById(productId).asFlow().map { it.singleOrNull()?.toProduct() }
	
	private fun List<ProductEntity>.toProducts() = map { it.toProduct() }
	
	private fun ProductEntity.toProduct() = Product(id, "", name, available, parameters)

	private fun List<Product>.toProductEntities() = map { it.toProductEntity() }
	
	private fun Product.toProductEntity() = ProductEntity(id, name, available, parameters)
}
