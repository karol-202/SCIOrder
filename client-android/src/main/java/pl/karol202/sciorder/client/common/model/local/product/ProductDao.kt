package pl.karol202.sciorder.client.common.model.local.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.client.common.model.local.CrudDao
import pl.karol202.sciorder.common.model.Product

interface ProductDao : CrudDao<Product>
{
	override fun insert(items: List<Product>)

	override fun update(items: List<Product>)

	override fun delete(items: List<Product>)

	override fun getAll(): LiveData<List<Product>>
}
