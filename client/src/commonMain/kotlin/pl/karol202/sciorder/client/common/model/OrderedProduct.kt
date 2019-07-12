package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.JvmSerializable
import pl.karol202.sciorder.common.Product

data class OrderedProduct(val id: String, // Exist only for Android adapter to be able to handle it
                          val product: Product,
                          val quantity: Int,
                          val parameters: Map<String, String>) : JvmSerializable
{
	companion object
}
