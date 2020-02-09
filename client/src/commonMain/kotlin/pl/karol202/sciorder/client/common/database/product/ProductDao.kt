package pl.karol202.sciorder.client.common.database.product

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.client.common.database.CrudDao
import pl.karol202.sciorder.common.model.Product

interface ProductDao : CrudDao<Product>
{
	suspend fun deleteAll()
	
	fun getById(id: String): Flow<Product?>
}
