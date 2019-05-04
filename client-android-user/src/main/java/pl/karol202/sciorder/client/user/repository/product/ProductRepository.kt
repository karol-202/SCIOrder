package pl.karol202.sciorder.client.user.repository.product

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.client.user.repository.Resource

interface ProductRepository
{
	fun getAllProducts(): Resource<List<Product>>
}
