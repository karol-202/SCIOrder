package pl.karol202.sciorder.client.android.user.ui.listener

import pl.karol202.sciorder.client.android.common.model.OrderedProduct

interface OnProductOrderEditListener
{
	fun onProductOrderEdit(oldProduct: OrderedProduct, newProduct: OrderedProduct)
}
