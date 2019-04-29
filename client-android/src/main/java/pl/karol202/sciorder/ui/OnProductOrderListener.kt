package pl.karol202.sciorder.ui

import pl.karol202.sciorder.model.Product

interface OnProductOrderListener
{
	fun onProductOrder(product: Product, quantity: Int, parameters: Map<String, String>)
}