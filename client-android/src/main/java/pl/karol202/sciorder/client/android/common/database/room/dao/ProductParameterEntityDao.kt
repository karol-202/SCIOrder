package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.ProductParameterWithEnumValues

@Dao
interface ProductParameterEntityDao : CrudDao<ProductParameterEntity>
{
	@Insert
	override suspend fun insert(items: List<ProductParameterEntity>)
	
	@Update
	override suspend fun update(items: List<ProductParameterEntity>)
	
	@Delete
	override suspend fun delete(items: List<ProductParameterEntity>)
	
	@Transaction
	@Query("SELECT * FROM ProductParameterEntity WHERE id = :parameterId")
	fun getById(parameterId: Long): Flow<ProductParameterWithEnumValues?>
	
	@Transaction
	@Query("SELECT * FROM ProductParameterEntity WHERE id IN(:parameterIds)")
	fun getByIds(parameterIds: List<Long>): Flow<List<ProductParameterWithEnumValues>>
}
