package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.room.entity.StoreEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.StoreWithProductsAndOrders

@Dao
interface StoreEntityDao : CrudDao<StoreEntity>
{
	@Insert
	override suspend fun insert(items: List<StoreEntity>)
	
	@Update
	override suspend fun update(items: List<StoreEntity>)
	
	@Delete
	override suspend fun delete(items: List<StoreEntity>)
	
	// TODO Verify behaviour with null storeId
	@Query("UPDATE StoreEntity SET selected = CASE WHEN id = :storeId THEN 1 ELSE 0 END")
	suspend fun updateSelection(storeId: Long?)
	
	@Query("DELETE FROM StoreEntity")
	suspend fun deleteAll()
	
	@Transaction
	@Query("SELECT * FROM StoreEntity WHERE id = :storeId")
	fun getById(storeId: Long): Flow<StoreWithProductsAndOrders?>
	
	@Transaction
	@Query("SELECT * FROM StoreEntity WHERE selected = 1")
	fun getSelected(): Flow<StoreWithProductsAndOrders?>
	
	@Transaction
	@Query("SELECT * FROM StoreEntity")
	fun getAll(): Flow<List<StoreWithProductsAndOrders>>
}
