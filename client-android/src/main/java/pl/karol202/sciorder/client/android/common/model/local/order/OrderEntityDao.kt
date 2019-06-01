package pl.karol202.sciorder.client.android.common.model.local.order

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.karol202.sciorder.common.model.Order

@Dao
interface OrderEntityDao
{
	@Insert
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
	fun getAll(): LiveData<List<OrderEntity>>

	@Query("SELECT * FROM ${OrderEntity.TABLE_NAME} WHERE ownerId = :ownerId")
	fun getByOwnerId(ownerId: String): LiveData<List<OrderEntity>>
}
