package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEntity
import pl.karol202.sciorder.client.common.database.dao.CrudDao

@Dao
interface ProductParameterEntityDao : CrudDao<ProductParameterEntity>
{
	@Insert
	override suspend fun insert(items: List<ProductParameterEntity>)
	
	@Update
	override suspend fun update(items: List<ProductParameterEntity>)
	
	@Delete
	override suspend fun delete(items: List<ProductParameterEntity>)
}
