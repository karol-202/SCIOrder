package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEnumValueEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.dispatch
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEnumValueEntity
import pl.karol202.sciorder.client.common.database.dao.ProductParameterDao
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.ids

class ProductParameterDaoImpl(private val localDatabase: LocalDatabase,
                              private val productParameterEntityDao: ProductParameterEntityDao,
                              private val productParameterEnumValueEntityDao: ProductParameterEnumValueEntityDao) :
		ProductParameterDao
{
	override suspend fun insert(items: List<ProductParameter>) = localDatabase.withTransaction {
		val parameterEntities = items.map { it.toEntity() }
		productParameterEntityDao.insert(parameterEntities)
		
		val enumValueEntities = items.flatMap { it.attributes.enumValues?.toEntities(it.id).orEmpty() }
		productParameterEnumValueEntityDao.insert(enumValueEntities)
	}
	
	override suspend fun update(items: List<ProductParameter>) = localDatabase.withTransaction {
		val oldData = productParameterEntityDao.getByIds(items.ids()).first()
		
		val newParameterEntities = items.filter { it.id in oldData.map { it.parameter }.ids() }.map { it.toEntity() }
		productParameterEntityDao.update(newParameterEntities)
		
		val oldEnumValueEntities = oldData.flatMap { it.enumValues }
		val newEnumValueEntities = items.filter { it.id in oldData.map { it.parameter }.ids() }
				.flatMap { it.attributes.enumValues?.toEntities(it.id).orEmpty() }
		productParameterEnumValueEntityDao.dispatch(oldEnumValueEntities, newEnumValueEntities)
	}
	
	override suspend fun delete(items: List<ProductParameter>) = productParameterEntityDao.delete(items.map { it.toEntity() })
	
	override fun getById(parameterId: Long) = productParameterEntityDao.getById(parameterId).map { it?.map() }
	
	private fun ProductParameter.toEntity() =
			ProductParameterEntity(id, productId, name, type,
			                       attributes.minimalValue, attributes.maximalValue, attributes.defaultValue)
	
	private fun List<String>.toEntities(productParameterId: Long) = map {
		ProductParameterEnumValueEntity(0, productParameterId, it)
	}
}
