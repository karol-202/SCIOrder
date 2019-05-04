package pl.karol202.sciorder.client.common.model.local.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.common.model.Product

interface ProductDao
{
	fun insertProducts(products: List<Product>)

	fun deleteProducts()

	fun getAllProducts(): LiveData<List<Product>>
}
