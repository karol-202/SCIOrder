package pl.karol202.sciorder.client.android.common.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.android.common.database.entity.OrderEntity
import pl.karol202.sciorder.common.model.Order

@Dao
interface OrderEntityDao
{
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(orders: List<OrderEntity>)

	@Update
	suspend fun update(orders: List<OrderEntity>)

	@Query("UPDATE ${OrderEntity.TABLE_NAME} SET status = :status WHERE id = :id")
	suspend fun updateStatus(id: String, status: Order.Status)

	@Delete
	suspend fun delete(orders: List<OrderEntity>)

	@Query("DELETE FROM ${OrderEntity.TABLE_NAME}")
	suspend fun deleteAll()

	@Query("SELECT * FROM ${OrderEntity.TABLE_NAME}")
	fun getAll(): Flow<List<OrderEntity>>

	@Query("SELECT * FROM ${OrderEntity.TABLE_NAME} WHERE ownerId = :ownerId")
	fun getByOwnerId(ownerId: String): Flow<List<OrderEntity>>
	
	@Query("SELECT status FROM ${OrderEntity.TABLE_NAME} WHERE id = :id")
	fun getStatus(id: String): Flow<Order.Status?>
}
