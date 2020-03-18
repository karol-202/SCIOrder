package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.karol202.sciorder.client.android.common.database.room.LocalDatabase
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.ProductParameterEnumValueEntityDao
import pl.karol202.sciorder.client.android.common.database.room.dao.dispatch
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.ProductParameterWithEnumValues
import pl.karol202.sciorder.client.android.common.database.room.relations.enumValues
import pl.karol202.sciorder.client.android.common.database.room.relations.parameters
import pl.karol202.sciorder.client.android.common.util.toEntities
import pl.karol202.sciorder.client.android.common.util.toModel
import pl.karol202.sciorder.client.common.database.dao.ProductParameterDao
import pl.karol202.sciorder.common.model.ProductParameter
import pl.karol202.sciorder.common.util.ids

class ProductParameterDaoImpl(private val localDatabase: LocalDatabase,
                              private val productParameterEntityDao: ProductParameterEntityDao,
                              private val productParameterEnumValueEntityDao: ProductParameterEnumValueEntityDao) :
		ProductParameterDao
{
	override suspend fun insert(items: List<ProductParameter>) = localDatabase.withTransaction {
		val entities = items.toEntities(ProductParameterWithEnumValues)
		
		productParameterEntityDao.insert(entities.parameters)
		productParameterEnumValueEntityDao.insert(entities.enumValues)
	}
	
	override suspend fun update(items: List<ProductParameter>) = localDatabase.withTransaction {
		val oldEntities = productParameterEntityDao.getByIds(items.ids()).first()
		val newEntities = items.toEntities(ProductParameterWithEnumValues).filter { it.parameter.id in oldEntities.parameters.ids() }
		
		productParameterEntityDao.update(newEntities.parameters)
		productParameterEnumValueEntityDao.dispatch(oldEntities.enumValues, newEntities.enumValues)
	}
	
	override suspend fun delete(items: List<ProductParameter>) =
			productParameterEntityDao.delete(items.toEntities(ProductParameterEntity))
	
	override fun getById(parameterId: Long) =
			productParameterEntityDao.getById(parameterId).map { it?.toModel(ProductParameterWithEnumValues) }
}
