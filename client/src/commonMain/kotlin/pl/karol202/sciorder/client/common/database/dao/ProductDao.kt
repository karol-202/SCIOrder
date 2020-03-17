package pl.karol202.sciorder.client.common.database.dao

import kotlinx.coroutines.flow.Flow
import pl.karol202.sciorder.common.model.Product

interface ProductDao : InsertDao<Product>, UpdateDao<Product>, DeleteDao<Product>
{
	suspend fun deleteAll()
	
	suspend fun dispatchByStoreId(storeId: Long, newProducts: List<Product>)
	
	fun getById(productId: Long): Flow<Product?>
	
	fun getByStoreId(storeId: Long): Flow<List<Product>>
}
