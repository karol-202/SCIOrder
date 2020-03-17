package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.ProductWithParameters

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
	
	@Query("SELECT * FROM ProductEntity WHERE id = :productId")
	fun getById(productId: Long): Flow<ProductWithParameters?>
	
	@Query("SELECT * FROM ProductEntity WHERE id IN(:productIds)")
	fun getByIds(productIds: List<Long>): Flow<List<ProductWithParameters>>
	
	@Query("SELECT * FROM ProductEntity WHERE storeId = :storeId")
	fun getByStoreId(storeId: Long): Flow<List<ProductWithParameters>>
}
