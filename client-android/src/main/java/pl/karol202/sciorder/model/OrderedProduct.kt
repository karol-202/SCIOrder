package pl.karol202.sciorder.model

import java.io.Serializable

data class OrderedProduct(val id: String,
                          val product: Product,
                          val quantity: Int,
                          val parameters: Map<String, String>) : Serializable