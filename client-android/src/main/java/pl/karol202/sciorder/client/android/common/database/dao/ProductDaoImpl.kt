package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEnumValueEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.dispatch
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.ProductWithParameters
import pl.karol202.sciorder.client.android.common.database.room.relations.enumValues
import pl.karol202.sciorder.client.android.common.database.room.relations.parameters
import pl.karol202.sciorder.client.android.common.database.room.relations.products
import pl.karol202.sciorder.client.android.common.database.room.toEntities
import pl.karol202.sciorder.client.android.common.database.room.toModel
import pl.karol202.sciorder.client.android.common.database.room.toModels
import pl.karol202.sciorder.client.common.database.dao.ProductDao
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.util.ids

class ProductDaoImpl(private val localDatabase: LocalDatabase,
                     private val productEntityDao: ProductEntityDao,
                     private val productParameterEntityDao: ProductParameterEntityDao,
                     private val productParameterEnumValueEntityDao: ProductParameterEnumValueEntityDao) : ProductDao
{
	override suspend fun insert(items: List<Product>) = localDatabase.withTransaction {
		val entities = items.toEntities(ProductWithParameters)
		
		productEntityDao.insert(entities.products)
		productParameterEntityDao.insert(entities.parameters)
		productParameterEnumValueEntityDao.insert(entities.enumValues)
	}
	
	override suspend fun update(items: List<Product>) = localDatabase.withTransaction {
		val oldEntities = productEntityDao.getByIds(items.ids()).first()
		val newEntities = items.toEntities(ProductWithParameters).filter { it.product.id in oldEntities.products.ids() }
		
		productEntityDao.update(newEntities.products)
		productParameterEntityDao.dispatch(oldEntities.parameters, newEntities.parameters)
		productParameterEnumValueEntityDao.dispatch(oldEntities.enumValues, newEntities.enumValues)
	}
	
	override suspend fun delete(items: List<Product>) = productEntityDao.delete(items.toEntities(ProductEntity))
	
	override suspend fun deleteAll() = productEntityDao.deleteAll()
	
	override suspend fun dispatchByStoreId(storeId: Long, newProducts: List<Product>) = localDatabase.withTransaction {
		val oldEntities = productEntityDao.getByStoreId(storeId).first()
		val newEntities = newProducts.toEntities(ProductWithParameters)
		
		productEntityDao.dispatch(oldEntities.products, newEntities.products)
		productParameterEntityDao.dispatch(oldEntities.parameters, newEntities.parameters)
		productParameterEnumValueEntityDao.dispatch(oldEntities.enumValues, newEntities.enumValues)
	}
	
	override fun getById(productId: Long) = productEntityDao.getById(productId).map { it?.toModel(ProductWithParameters) }
	
	override fun getByStoreId(storeId: Long) = productEntityDao.getByStoreId(storeId).map { it.toModels(ProductWithParameters) }
}

