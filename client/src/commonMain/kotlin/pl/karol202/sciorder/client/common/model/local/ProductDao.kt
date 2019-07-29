package pl.karol202.sciorder.client.common.model.local

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.Product

interface ProductDao : CrudDao<Product>
{
	suspend fun deleteAll()
	
	fun getById(id: String): Flow<Product?>
}
