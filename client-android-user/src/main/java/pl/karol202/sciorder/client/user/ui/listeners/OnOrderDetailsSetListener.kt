package pl.karol202.sciorder.client.user.ui.listeners

import pl.karol202.sciorder.common.model.Order
import pl.karol202.sciorder.client.user.model.OrderedProduct
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
