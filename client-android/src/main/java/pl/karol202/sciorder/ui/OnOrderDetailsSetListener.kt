package pl.karol202.sciorder.ui

import pl.karol202.sciorder.model.Order
import pl.karol202.sciorder.model.OrderedProduct
import java.io.Serializable

interface OnOrderDetailsSetListener
{
	sealed class Case : Serializable
	{
		data class OrderSingle(val orderedProduct: OrderedProduct) : Case()
		{
			override val orderedProducts = listOf(orderedProduct)
		}

		data class OrderAll(override val orderedProducts: List<OrderedProduct>) : Case()

		abstract val orderedProducts: List<OrderedProduct>
	}

	fun onOrderDetailsSet(case: Case, details: Order.Details)
}