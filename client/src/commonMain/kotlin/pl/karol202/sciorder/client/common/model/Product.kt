package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.client.common.util.uuid
import pl.karol202.sciorder.common.Product

fun Product.Companion.create(id: String = uuid(), name: String = "") = Product(id, "", name, true, emptyList())

val Product.Parameter.Companion.NEW_PARAMETER
	get() = Product.Parameter("", Product.Parameter.Type.TEXT, Product.Parameter.Attributes())
