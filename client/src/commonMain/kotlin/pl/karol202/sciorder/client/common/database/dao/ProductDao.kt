package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.Product

interface ProductDao : CrudDao<Product>
{
	suspend fun deleteAll()
	
	fun getById(id: String): Flow<Product?>
}
