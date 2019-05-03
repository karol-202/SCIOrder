package pl.karol202.sciorder.ui.listeners

import pl.karol202.sciorder.model.OrderedProduct

interface OnProductOrderEditListener
{
	fun onProductOrderEdit(oldProduct: OrderedProduct, newProduct: OrderedProduct)
}