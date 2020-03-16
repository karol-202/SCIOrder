package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity
import pl.karol202.sciorder.client.common.database.dao.CrudDao

@Dao
interface ProductEntityDao : CrudDao<ProductEntity>
{
	@Insert
	override suspend fun insert(items: List<ProductEntity>)

	@Update
	override suspend fun update(items: List<ProductEntity>)

	@Delete
	override suspend fun delete(items: List<ProductEntity>)

	@Query("DELETE FROM ProductEntity")
	suspend fun deleteAll()
	
	@Query("SELECT * FROM ProductEntity WHERE id = :id")
	fun getById(id: Long): Flow<ProductEntity>
	
	@Query("SELECT * FROM ProductEntity WHERE storeId = :storeId")
	fun getByStoreId(storeId: Long): Flow<List<ProductEntity>>
}
