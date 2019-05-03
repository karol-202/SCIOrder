package pl.karol202.sciorder.model.local.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Product

interface ProductDao
{
	fun insertProducts(products: List<Product>)

	fun deleteProducts()

	fun getAllProducts(): LiveData<List<Product>>
}
