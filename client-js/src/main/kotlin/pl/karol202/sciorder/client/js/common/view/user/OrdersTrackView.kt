package pl.karol202.sciorder.client.js.common.view.user

import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mTypography
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.margin
import kotlinx.css.marginLeft
import kotlinx.css.marginRight
import kotlinx.css.px
import pl.karol202.sciorder.client.common.model.OrderedProduct
import pl.karol202.sciorder.client.js.common.model.color
import pl.karol202.sciorder.client.js.common.model.create
import pl.karol202.sciorder.client.js.common.model.visibleName
import pl.karol202.sciorder.client.js.common.util.flexBox
import pl.karol202.sciorder.client.js.common.util.overrideCss
import pl.karol202.sciorder.client.js.common.util.prop
import pl.karol202.sciorder.client.js.common.view.View
import pl.karol202.sciorder.common.Order
import pl.karol202.sciorder.common.Product
import react.RBuilder
import react.RProps
import react.RState
import styled.css

class OrdersTrackView : View<OrdersTrackView.Props, RState>()
{
	interface Props : RProps
	{
		var orders: List<Order>
		var products: List<Product>
	}
	
	companion object
	{
		private val PLACEHOLDER_PRODUCT = Product("", "", "Usunięty produkt", false, emptyList())
	}
	
	private val orders by prop { orders }
	private val products by prop { products }
	
	override fun RBuilder.render()
	{
		flexBox(direction = FlexDirection.column) {
			titleText()
			orders()
		}
	}
	
	private fun RBuilder.titleText() = mTypography(text = "Twoje zamówienia",
	                                               variant = MTypographyVariant.h6) {
		overrideCss {
			margin(16.px)
		}
	}
	
	private fun RBuilder.orders() = mList {
		css {
			display = Display.flex
			flexDirection = FlexDirection.row
		}
		
		orders.forEach { order(it) }
	}
	
	private fun RBuilder.order(order: Order) = mCard {
		css {
			marginLeft = 16.px
			marginRight = 8.px
		}
		
		orderStatusText(order.status)
		products(order)
	}
	
	private fun RBuilder.orderStatusText(status: Order.Status) =
			mTypography(text = "Status: ${status.visibleName}",
			            variant = MTypographyVariant.subtitle2) {
		overrideCss {
			color = status.color
			margin(16.px)
		}
	}
	
	private fun RBuilder.products(order: Order) = orderedProductsView(order.getOrderedProducts())
	
	private fun Order.getOrderedProducts() = entries.map { it.getOrderedProduct() }
	
	private fun Order.Entry.getOrderedProduct() =
			OrderedProduct.create(products.find { it.id == this.productId } ?: PLACEHOLDER_PRODUCT, quantity, parameters)
}

fun RBuilder.ordersTrackView(orders: List<Order>,
                             products: List<Product>) = child(OrdersTrackView::class) {
	attrs.orders = orders
	attrs.products = products
}
