package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.client.common.util.uuid
import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.util.JvmSerializable

data class OrderedProduct(val id: String,
                          val product: Product,
                          val quantity: Int,
                          val parameters: Map<String, String>) : JvmSerializable
{
	companion object
	{
		fun create(product: Product, quantity: Int, parameters: Map<String, String>) =
				OrderedProduct(uuid(), product, quantity, parameters)
	}
}
