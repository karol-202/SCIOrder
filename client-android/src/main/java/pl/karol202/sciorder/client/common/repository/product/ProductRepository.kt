package pl.karol202.sciorder.client.common.repository.product

import pl.karol202.sciorder.client.common.repository.resource.Resource
import pl.karol202.sciorder.common.model.Product

interface ProductRepository
{
	fun getAllProducts(ownerId: String): Resource<List<Product>>
}
