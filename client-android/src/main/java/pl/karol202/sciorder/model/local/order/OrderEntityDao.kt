package pl.karol202.sciorder.model.local.order

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.karol202.sciorder.model.Order

@Dao
interface OrderEntityDao
{
	@WorkerThread
	@Query("DELETE FROM ${OrderEntity.TABLE_NAME}")
	fun clearOrders()

	@WorkerThread
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOrders(orders: List<OrderEntity>)

	@Query("SELECT * FROM ${OrderEntity.TABLE_NAME}")
	fun getAllOrders(): LiveData<List<OrderEntity>>
}
