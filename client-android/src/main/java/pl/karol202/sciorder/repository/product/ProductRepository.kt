package pl.karol202.sciorder.repository.product

import pl.karol202.sciorder.model.Product
import pl.karol202.sciorder.repository.Resource

interface ProductRepository
{
	fun getAllProducts(): Resource<List<Product>>
}
