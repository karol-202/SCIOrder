package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.ProductParameterWithEnumValues

interface ProductParameterEntityDao : CrudDao<ProductParameterEntity>
{
	@Insert
	override suspend fun insert(items: List<ProductParameterEntity>)
	
	@Update
	override suspend fun update(items: List<ProductParameterEntity>)
	
	@Delete
	override suspend fun delete(items: List<ProductParameterEntity>)
	
	@Query("SELECT * FROM ProductParameterEntity WHERE id = :parameterId")
	fun getById(parameterId: Long): Flow<ProductParameterWithEnumValues?>
	
	@Query("SELECT * FROM ProductParameterEnumValueEntity WHERE id IN(:parameterIds)")
	fun getByIds(parameterIds: List<Long>): Flow<List<ProductParameterWithEnumValues>>
}
