package pl.karol202.sciorder.client.common.model.local.order

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderEntityDao
{
	@WorkerThread
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOrder(order: OrderEntity)

	@WorkerThread
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOrders(orders: List<OrderEntity>)

	@WorkerThread
	@Delete
	fun deleteOrder(order: OrderEntity)

	@WorkerThread
	@Query("DELETE FROM ${OrderEntity.TABLE_NAME}")
	fun deleteOrders()

	@Query("SELECT * FROM ${OrderEntity.TABLE_NAME}")
	fun getAllOrders(): LiveData<List<OrderEntity>>
}
