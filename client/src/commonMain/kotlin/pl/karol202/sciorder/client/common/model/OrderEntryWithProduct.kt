package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.model.OrderEntry
import pl.karol202.sciorder.common.model.Product

data class OrderEntryWithProduct(val id: Long,
                                 val product: Product?,
                                 val quantity: Int,
                                 val parameters: Map<Long, String>)

fun List<OrderEntry>.withProducts(products: List<Product>) = map { it.withProduct(products) }

fun OrderEntry.withProduct(products: List<Product>) =
		OrderEntryWithProduct(id, products.find { it.id == productId }, quantity, parameters)
