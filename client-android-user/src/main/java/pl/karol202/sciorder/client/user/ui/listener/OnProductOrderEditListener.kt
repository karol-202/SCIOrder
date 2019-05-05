package pl.karol202.sciorder.client.user.ui.listener

import pl.karol202.sciorder.client.common.model.OrderedProduct

interface OnProductOrderEditListener
{
	fun onProductOrderEdit(oldProduct: OrderedProduct, newProduct: OrderedProduct)
}
