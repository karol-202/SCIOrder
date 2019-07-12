package pl.karol202.sciorder.client.js.common.model

import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.common.Product

fun OrderedProduct.Companion.create(product: Product, quantity: Int, parameters: Map<String, String>) =
		OrderedProduct("", product, quantity, parameters)
