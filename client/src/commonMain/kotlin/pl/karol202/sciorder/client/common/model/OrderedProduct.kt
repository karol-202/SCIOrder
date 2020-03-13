package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.model.Product
import pl.karol202.sciorder.common.util.JvmSerializable

data class OrderedProduct(val id: String,
                          val product: Product,
                          val quantity: Int,
                          val parameters: Map<Long, String>) : JvmSerializable
