package pl.karol202.sciorder.client.android.user.ui.listener

import pl.karol202.sciorder.client.common.model.OrderedProduct

interface OnProductOrderListener
{
	fun onProductOrder(orderedProduct: OrderedProduct)

	fun onProductAddToOrder(orderedProduct: OrderedProduct)
}
