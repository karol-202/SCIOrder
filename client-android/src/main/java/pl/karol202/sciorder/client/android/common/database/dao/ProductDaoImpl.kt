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
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEnumValueEntity
import pl.karol202.sciorder.client.common.database.dao.ProductDao
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.ids
import pl.karol202.sciorder.common.util.map

class ProductDaoImpl(private val localDatabase: LocalDatabase,
                     private val productEntityDao: ProductEntityDao,
                     private val productParameterEntityDao: ProductParameterEntityDao,
                     private val productParameterEnumValueEntityDao: ProductParameterEnumValueEntityDao) : ProductDao
{
	override suspend fun insert(items: List<Product>) = localDatabase.withTransaction {
		val productEntities = items.map { it.toEntity() }
		productEntityDao.insert(productEntities)
		
		val parameterEntities = items.flatMap { it.parameters }.map { it.toEntity() }
		productParameterEntityDao.insert(parameterEntities)
		
		val enumValueEntities =
				items.flatMap { it.parameters }.flatMap { it.attributes.enumValues?.toEntities(it.id).orEmpty() }
		productParameterEnumValueEntityDao.insert(enumValueEntities)
	}
	
	override suspend fun update(items: List<Product>) = localDatabase.withTransaction {
		val oldData = productEntityDao.getByIds(items.ids()).first()
		
		val newProductEntities = items.filter { it.id in oldData.map { it.product }.ids() }.map { it.toEntity() }
		productEntityDao.update(newProductEntities)
		
		val oldParameterEntities = oldData.flatMap { it.parameters }.map { it.parameter }
		val newParameterEntities = items.filter { it.id in items.ids() }.flatMap { it.parameters }.map { it.toEntity() }
		productParameterEntityDao.dispatch(oldParameterEntities, newParameterEntities)
		
		val oldEnumValueEntities = oldData.flatMap { it.parameters }.flatMap { it.enumValues }
		val newEnumValueEntities = items.filter { it.id in items.ids() }.flatMap { it.parameters }
				.flatMap { it.attributes.enumValues?.toEntities(it.id).orEmpty() }
		productParameterEnumValueEntityDao.dispatch(oldEnumValueEntities, newEnumValueEntities)
	}
	
	override suspend fun delete(items: List<Product>) = productEntityDao.delete(items.map { it.toEntity() })
	
	override suspend fun deleteAll() = productEntityDao.deleteAll()
	
	override suspend fun dispatchByStoreId(storeId: Long, newProducts: List<Product>) = localDatabase.withTransaction {
		val oldData = productEntityDao.getByStoreId(storeId).first()
		
		val oldProductEntities = oldData.map { it.product }
		val newProductEntities = newProducts.map { it.toEntity() }
		productEntityDao.dispatch(oldProductEntities, newProductEntities)
		
		val oldParameterEntities = oldData.flatMap { it.parameters }.map { it.parameter }
		val newParameterEntities = newProducts.flatMap { it.parameters }.map { it.toEntity() }
		productParameterEntityDao.dispatch(oldParameterEntities, newParameterEntities)
		
		val oldEnumValueEntities = oldData.flatMap { it.parameters }.flatMap { it.enumValues }
		val newEnumValueEntities =
				newProducts.flatMap { it.parameters }.flatMap { it.attributes.enumValues?.toEntities(it.id).orEmpty() }
		productParameterEnumValueEntityDao.dispatch(oldEnumValueEntities, newEnumValueEntities)
	}
	
	override fun getById(productId: Long) = productEntityDao.getById(productId).map { it?.map() }
	
	override fun getByStoreId(storeId: Long) = productEntityDao.getByStoreId(storeId).map { it.map() }
	
	private fun Product.toEntity() = ProductEntity(id, storeId, name, available)
	
	private fun ProductParameter.toEntity() =
			ProductParameterEntity(id, productId, name, type,
			                       attributes.minimalValue, attributes.maximalValue, attributes.defaultValue)
	
	private fun List<String>.toEntities(productParameterId: Long) = map {
		ProductParameterEnumValueEntity(0, productParameterId, it)
	}
}

