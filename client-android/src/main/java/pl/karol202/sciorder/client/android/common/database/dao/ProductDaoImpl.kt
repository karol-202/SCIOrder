package pl.karol202.sciorder.client.android.common.database.dao

import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEnumValueEntityDao
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.util.toEntity
import pl.karol202.sciorder.client.android.common.util.toModel
import pl.karol202.sciorder.client.common.database.dao.ProductDao
import pl.karol202.sciorder.common.model.Product

class ProductDaoImpl(private val productEntityDao: ProductEntityDao,
                     private val productParameterEntityDao: ProductParameterEntityDao,
                     private val productParameterEnumValueEntityDao: ProductParameterEnumValueEntityDao) : ProductDao
{
	override suspend fun insert(items: List<Product>)
	{
		productEntityDao.insert(items.toEntity(ProductEntity))
		
		val parameters = items.flatMap { it.parameters }
		productParameterEntityDao.insert(parameters.toEntity(ProductParameterEntity))
		
		val enumValues = parameters.flatMap { it.attributes.enumValues }
	}
	
	override suspend fun update(items: List<Product>) = productEntityDao.update(items.toEntity(ProductEntity))
	
	override suspend fun delete(items: List<Product>) = productEntityDao.delete(items.toEntity(ProductEntity))
	
	override suspend fun deleteAll() = productEntityDao.deleteAll()
	
	override fun getById(productId: Long) = productEntityDao.getById(productId).map { it.toModel(ProductEntity) }
	
	override fun getByStoreId(storeId: Long) = productEntityDao.getByStoreId(storeId).map { it.toModel(ProductEntity) }
}
