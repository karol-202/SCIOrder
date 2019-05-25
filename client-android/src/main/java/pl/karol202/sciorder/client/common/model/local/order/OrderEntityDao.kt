package pl.karol202.sciorder.client.common.model.local.order

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import pl.karol202.sciorder.common.model.Order

@Dao
interface OrderEntityDao
{
	@WorkerThread
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(orders: List<OrderEntity>)

	@WorkerThread
	@Update
	fun update(orders: List<OrderEntity>)

	@WorkerThread
	@Query("UPDATE ${OrderEntity.TABLE_NAME} SET status = :status WHERE id = :id")
	fun updateStatus(id: String, status: Order.Status)

	@WorkerThread
	@Delete
	fun delete(orders: List<OrderEntity>)

	@WorkerThread
	@Query("DELETE FROM ${OrderEntity.TABLE_NAME}")
	fun deleteAll()

	@Query("SELECT * FROM ${OrderEntity.TABLE_NAME}")
	fun getAll(): LiveData<List<OrderEntity>>
}
