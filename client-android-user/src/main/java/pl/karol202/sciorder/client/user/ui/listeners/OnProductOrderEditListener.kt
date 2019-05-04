package pl.karol202.sciorder.client.user.ui.listeners

import pl.karol202.sciorder.client.user.model.OrderedProduct

interface OnProductOrderEditListener
{
	fun onProductOrderEdit(oldProduct: OrderedProduct, newProduct: OrderedProduct)
}
