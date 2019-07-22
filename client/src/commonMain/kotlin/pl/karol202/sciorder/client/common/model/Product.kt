package pl.karol202.sciorder.client.common.model

import pl.karol202.sciorder.common.Product

val Product.Companion.NEW_PRODUCT get() = Product("", "", "", true, emptyList())

val Product.Parameter.Companion.NEW_PARAMETER
	get() = Product.Parameter("", Product.Parameter.Type.TEXT, Product.Parameter.Attributes())
