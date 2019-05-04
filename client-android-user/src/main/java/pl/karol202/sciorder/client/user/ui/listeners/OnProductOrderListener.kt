package pl.karol202.sciorder.client.user.ui.listeners

import pl.karol202.sciorder.client.user.model.OrderedProduct

interface OnProductOrderListener
{
	fun onProductOrder(orderedProduct: OrderedProduct)

	fun onProductAddToOrder(orderedProduct: OrderedProduct)
}
