package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEnumValueEntity
import pl.karol202.sciorder.client.common.database.dao.CrudDao

@Dao
interface ProductParameterEnumValueEntityDao : CrudDao<ProductParameterEnumValueEntity>
{
	@Insert
	override suspend fun insert(items: List<ProductParameterEnumValueEntity>)
	
	@Delete
	override suspend fun delete(items: List<ProductParameterEnumValueEntity>)
}
