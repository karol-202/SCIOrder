package pl.karol202.sciorder.model.local.product

import androidx.lifecycle.LiveData
import pl.karol202.sciorder.model.Product

interface ProductDao
{
	fun clearProducts()

	fun insertProducts(products: List<Product>)

	fun getAllProducts(): LiveData<List<Product>>
}