package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.ProductParameter

interface ProductParameterDao : CrudDao<ProductParameter>
{
	suspend fun deleteAll()
	
	fun getById(parameterId: Long): Flow<ProductParameter?>
	
	fun getByProductId(productId: Long): Flow<List<ProductParameter>>
}

