package pl.karol202.sciorder.model

data class OrderedProduct(val product: Product,
                          val quantity: Int,
                          val parameters: Map<String, String>)