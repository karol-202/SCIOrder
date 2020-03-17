package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryParameterValueEntity

@Dao
interface OrderEntryParameterValueEntityDao
{
	@Insert
	suspend fun insert(items: List<OrderEntryParameterValueEntity>)
	
	@Delete
	suspend fun delete(items: List<OrderEntryParameterValueEntity>)
}
