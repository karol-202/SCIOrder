package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.common.model.Product

data class OrderWithProducts(val id: Long,
                             val entries: List<OrderEntryWithProduct>,
                             val details: Order.Details,
                             val status: Order.Status)

fun List<Order>.withProducts(products: List<Product>) = map { it.withProducts(products) }

fun Order.withProducts(products: List<Product>) =
		OrderWithProducts(id, entries.withProducts(products), details, status)
