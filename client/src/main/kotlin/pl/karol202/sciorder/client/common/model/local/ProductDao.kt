package pl.karol202.sciorder.client.common.model.local

import pl.karol202.sciorder.common.model.Product

interface ProductDao : CrudDao<Product>
{
	suspend fun deleteAll()
}
