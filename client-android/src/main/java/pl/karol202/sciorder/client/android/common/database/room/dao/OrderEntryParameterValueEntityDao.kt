package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.karol202.sciorder.client.android.common.database.room.entity.OrderEntryParameterValueEntity

@Dao
interface OrderEntryParameterValueEntityDao : CrudDao<OrderEntryParameterValueEntity>
{
	@Insert
	override suspend fun insert(items: List<OrderEntryParameterValueEntity>)
	
	@Update
	override suspend fun update(items: List<OrderEntryParameterValueEntity>)
	
	@Delete
	override suspend fun delete(items: List<OrderEntryParameterValueEntity>)
}
