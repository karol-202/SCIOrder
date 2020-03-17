package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntity
import pl.karol202.sciorder.client.android.common.database.room.relations.OrderWithEntries
import pl.karol202.sciorder.common.model.Order

@Dao
interface OrderEntityDao : CrudDao<OrderEntity>
{
	@Insert
	override suspend fun insert(items: List<OrderEntity>)
	
	@Update
	override suspend fun update(items: List<OrderEntity>)
	
	@Delete
	override suspend fun delete(items: List<OrderEntity>)
	
	@Query("UPDATE OrderEntity SET status = :status WHERE id = :id")
	suspend fun updateStatus(id: Long, status: Order.Status)
	
	@Query("DELETE FROM OrderEntity")
	suspend fun deleteAll()
	
	@Query("DELETE FROM OrderEntity WHERE storeId = :storeId")
	suspend fun deleteByStoreId(storeId: Long)
	
	@Query("SELECT * FROM OrderEntity WHERE id = :orderId")
	fun getById(orderId: Long): Flow<OrderWithEntries?>
	
	@Query("SELECT * FROM OrderEntity WHERE storeId = :storeId")
	fun getByStoreId(storeId: Long): Flow<List<OrderWithEntries>>
}
