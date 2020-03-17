package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryEntity

@Dao
interface OrderEntryEntityDao : CrudDao<OrderEntryEntity>
{
	@Insert
	override suspend fun insert(items: List<OrderEntryEntity>)
	
	@Update
	override suspend fun update(items: List<OrderEntryEntity>)
	
	@Delete
	override suspend fun delete(items: List<OrderEntryEntity>)
}
