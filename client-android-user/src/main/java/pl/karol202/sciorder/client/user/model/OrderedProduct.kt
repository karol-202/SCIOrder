package pl.karol202.sciorder.client.user.model

import pl.karol202.sciorder.common.model.Product
import java.io.Serializable

data class OrderedProduct(val id: String,
                          val product: Product,
                          val quantity: Int,
                          val parameters: Map<String, String>) : Serializable
