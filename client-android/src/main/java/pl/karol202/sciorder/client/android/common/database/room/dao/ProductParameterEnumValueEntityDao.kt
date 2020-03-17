package pl.karol202.sciorder.client.android.common.database.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.karol202.sciorder.client.android.common.database.room.entity.ProductParameterEnumValueEntity

interface ProductParameterEnumValueEntityDao : CrudDao<ProductParameterEnumValueEntity>
{
	@Insert
	override suspend fun insert(items: List<ProductParameterEnumValueEntity>)
	
	@Update
	override suspend fun update(items: List<ProductParameterEnumValueEntity>)

	@Delete
	override suspend fun delete(items: List<ProductParameterEnumValueEntity>)
}
