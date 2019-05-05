package pl.karol202.sciorder.client.user.ui.listener

import pl.karol202.sciorder.client.common.model.OrderedProduct

interface OnProductOrderListener
{
	fun onProductOrder(orderedProduct: OrderedProduct)

	fun onProductAddToOrder(orderedProduct: OrderedProduct)
}
