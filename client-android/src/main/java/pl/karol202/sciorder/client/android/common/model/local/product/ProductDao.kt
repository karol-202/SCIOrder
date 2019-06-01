package pl.karol202.sciorder.client.android.common.model.local.product

import pl.karol202.sciorder.client.android.common.model.local.CrudDao
import pl.karol202.sciorder.common.model.Product

interface ProductDao : CrudDao<Product>
{
	suspend fun deleteAll()
}
