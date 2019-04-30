package pl.karol202.sciorder.ui

import pl.karol202.sciorder.model.OrderedProduct

interface OnProductOrderListener
{
	fun onProductOrder(orderedProduct: OrderedProduct)

	fun onProductAddToOrder(orderedProduct: OrderedProduct)
}